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
                IDataObject iData = Clipboard.GetDataObject();// Clipboard's data.

                /* Depending on the clipboard's current data format we can process the data differently. */
                if (iData.GetDataPresent(DataFormats.Text))
                {
                    string text = (string)iData.GetData(DataFormats.Text);

                    uploadTextData(text);
                }

                else if (iData.GetDataPresent(DataFormats.FileDrop))
                {
                    String image_path = ((string[])iData.GetData(DataFormats.FileDrop))[0];
                    string[] tmp = image_path.Split('.');
                    System.Drawing.Imaging.ImageFormat format = null;
                    supported_formats.TryGetValue(tmp[tmp.Length - 1], out format);

                    MemoryStream stream = new MemoryStream();
                    //image.Save(stream, System.Drawing.Imaging.ImageFormat.Png);


                    uploadMediaData(stream);

                }
            }
        }

        private async void uploadMediaData(MemoryStream data)
        {
            await api.Push(user_token, data);
        }

        private async void uploadTextData(String text) {
            await api.Push(user_token, text);
         
        }


        /// <summary>
        /// Work done by the background thread.Every 5 second the thread will do a pull from the server
        /// and check if the content of the clipboard has changed!
        /// </summary>
        public async void fetchInformation()
        {
            if (Properties.Settings.Default.userToken == 0)
                return;

            while (true) { 
                HttpResponseMessage response = await api.Pull(Properties.Settings.Default.userToken);

                Console.WriteLine(response.Content);
                Thread.Sleep(5000);
            }
        }
   
    }
}
