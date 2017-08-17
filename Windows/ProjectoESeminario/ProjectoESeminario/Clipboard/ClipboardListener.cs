using Projecto.Service;
using System;
using System.Collections.Generic;
using System.Runtime.InteropServices;
using System.Threading;
using System.Windows.Forms;
using System.IO;
using System.Drawing.Imaging;
using WebSocketSharp;
using System.Configuration;

namespace ProjectoESeminario
{

    public partial class ClipboardListener : Form, IClipboardListener
    {
        private ProjectoAPI api = new ProjectoAPI();
        private Dictionary<string, ImageFormat> supported_formats = new Dictionary<string, ImageFormat>();

        private String socket = ConfigurationManager.AppSettings["socketURL"];
        private String sub = Properties.Settings.Default.sub;
        private string deviceID = Properties.Settings.Default.deviceID;
        private volatile String lastClipboardContent = "";

        private readonly String TAG = "Portugal: ClipboardListener";
        private static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);

        /// <summary>
        /// Places the given window in the system-maintained clipboard format listener list.
        /// </summary>
        [DllImport("user32.dll", SetLastError = true)]
        [return: MarshalAs(UnmanagedType.Bool)]
        static extern bool AddClipboardFormatListener(IntPtr hwnd);

        /// <summary>
        /// Removes the given window from the system-maintained clipboard format listener list.
        /// </summary>
        [DllImport("user32.dll", SetLastError = true)]
        [return: MarshalAs(UnmanagedType.Bool)]
        static extern bool RemoveClipboardFormatListener(IntPtr hwnd);

        /// <summary>
        /// Code sent when the contents of the clipboard have changed.
        /// </summary>
        private const int WM_CLIPBOARDUPDATE = 0x031D;

        private readonly CancellationTokenSource cts;

        public ClipboardListener()
        {
            log.Info(TAG + " ctor");
            supported_formats.Add("png", ImageFormat.Png);
            supported_formats.Add("jpg", ImageFormat.Jpeg);

            cts = new CancellationTokenSource();

            if (Properties.Settings.Default.serviceEnabled)
                enableService();

        }

        protected override void WndProc(ref Message m)
        {
            base.WndProc(ref m);

            if (m.Msg == WM_CLIPBOARDUPDATE)
            {
                log.Debug(TAG + " Changes where detected in the clipboard");
                IDataObject iData = Clipboard.GetDataObject(); // Clipboard's data.

                /* Depending on the clipboard's current data format we can process the data differently. */
                if (iData.GetDataPresent(DataFormats.Text))
                {
                    log.Debug(TAG + " It was text");
                  
                    string text = (string)iData.GetData(DataFormats.Text);

                    uploadTextData(text);
                }

                else if (iData.GetDataPresent(DataFormats.FileDrop))
                {
                    log.Debug(TAG + " It could be an format that we support");
                    String image_path = ((string[])iData.GetData(DataFormats.FileDrop))[0];
                    string[] tmp = image_path.Split('\\');
                    string[] file_format = tmp[tmp.Length - 1].Split('.');
                    
                    ImageFormat format = null;
                    
                    if(supported_formats.TryGetValue(file_format[file_format.Length - 1], out format))
                    {
                        log.Debug(TAG + " Upload file to server");
                        byte[] image = File.ReadAllBytes(image_path);
                        
                        uploadMediaData(format, image, tmp[tmp.Length - 1]);

                    }
                }
            }
        }

        private async void uploadMediaData(ImageFormat format, byte[] image, string name)
        {
            await api.Push(sub, image, name, format.ToString().ToLower());
        }

        private async void uploadTextData(String text) {
            log.Info(TAG + " uploading text to the server");

            if (switchClipboardValue(text)) { 
                var response = await api.Push(sub, text);

                if(response == null)
                {
                    log.Error(TAG + " the upload FAILED!");
                    //should we try again?
                }
            }

        }

        private Boolean switchClipboardValue(string newValue)
        {
            string initialValue = lastClipboardContent;

            while (!initialValue.Equals(newValue))
            {

                if (!Interlocked.CompareExchange(ref lastClipboardContent, newValue, initialValue).Equals(initialValue))
                {
                    log.Debug(TAG + " will altering clipboard value");
                    return true;
                }
            }

            log.Debug(TAG + " wont switch clipboard variable state");
            return false;
        }


        public void fetchInformation(String socketUrl, CancellationToken cancelToken)
        {
            WebSocket ws = new WebSocket(socketUrl);
            ws.EmitOnPing = true;
            ws.OnMessage += (sender, e) => {

                Console.WriteLine("Laputa says: " + e.Data);

            };

            ws.Connect();

            ws.SendAsync(getUserInformation(), null);

            while (true)
            {
                if (cancelToken.IsCancellationRequested)
                {
                    if(ws != null)
                        ws.Close();

                    return;
                }


            }
        }

        private String getUserInformation()
        {
            return "{\n" +
                        "\"sub\":\"" + sub + "\",\n" +
                        "\"id\":\"" + deviceID +
                    "\"\n}";
        }

        private void storeText(string content)
        {
            log.Debug(TAG + "copying text to clipboard");

            //Clipboard cannot be called from backgroudn thread.This ensures that the setText will run on Main.
            Invoke((Action)(() => { Clipboard.SetText(content); }));
            
        }

        private void storeMIME(string content) {
            throw new NotImplementedException();
        }

        public void enableService()
        {
            AddClipboardFormatListener(this.Handle);
            Thread workingThread = new Thread(() => fetchInformation(socket ,cts.Token))
            { IsBackground = true };
        
            workingThread.Start();
        }

        public void disableService()
        {
            RemoveClipboardFormatListener(this.Handle);
            cts.Cancel();
        }
    }
}
