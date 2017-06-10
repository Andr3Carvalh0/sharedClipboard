using Projecto.Service;
using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Runtime.InteropServices;
using System.Threading;
using System.Windows.Forms;
using System.IO;

namespace ProjectoESeminario
{
    public partial class ClipboardListener : Form
    {
        ProjectoAPI api = new ProjectoAPI();
        Dictionary<string, System.Drawing.Imaging.ImageFormat> supported_formats = new Dictionary<string, System.Drawing.Imaging.ImageFormat>();
        long user_token = Properties.Settings.Default.userToken;

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

        public ClipboardListener()
        {
            log.Info(TAG + " ctor");
            supported_formats.Add(".png", System.Drawing.Imaging.ImageFormat.Png);
            supported_formats.Add(".jpg", System.Drawing.Imaging.ImageFormat.Jpeg);

            AddClipboardFormatListener(this.Handle);
            Thread workingThread = new Thread(() => fetchInformation())
            { IsBackground = true };

            workingThread.Start();
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
                    //String image_path = ((string[])iData.GetData(DataFormats.FileDrop))[0];
                    //string[] tmp = image_path.Split('.');
                    //System.Drawing.Imaging.ImageFormat format = null;
                    //supported_formats.TryGetValue(tmp[tmp.Length - 1], out format);

                    //MemoryStream stream = new MemoryStream();


                    //uploadMediaData(stream);

                }
            }
        }

        private async void uploadMediaData(MemoryStream data)
        {
            await api.Push(user_token, data);
        }

        private async void uploadTextData(String text) {
            log.Info(TAG + " uploading text to the server");

            var response = await api.Push(user_token, text);

            if(response == null)
            {
                log.Error(TAG + " the upload FAILED!");
                //should we try again?
            }
         
        }


        /// <summary>
        /// Work done by the background thread.Every 30 second the thread will do a pull from the server
        /// and check if the content of the clipboard has changed!
        /// </summary>
        public async void fetchInformation()
        {
            while (true) { 
                HttpResponseMessage response = await api.Pull(user_token);

                if(response.StatusCode == System.Net.HttpStatusCode.OK){
                    log.Debug(TAG + "Thread: Obtain something");
                    var body = await response.Content.ReadAsStringAsync();
                    Console.WriteLine(body);
                }

                log.Debug(TAG + "Thread: Going to sleep a little, bye.");
                Thread.Sleep(30000);
            }
        }
   
    }
}
