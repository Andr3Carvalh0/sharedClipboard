using Projecto.Service;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Net.Http;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace ProjectoESeminario
{
    public partial class ClipboardListener : Form
    {
        bool isUploading = false;
        ProjectoAPI api = new ProjectoAPI();

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
        /// Sent when the contents of the clipboard have changed.
        /// </summary>
        private const int WM_CLIPBOARDUPDATE = 0x031D;

        public ClipboardListener()
        {
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
                IDataObject iData = Clipboard.GetDataObject();      // Clipboard's data.

                /* Depending on the clipboard's current data format we can process the data differently. 
                 * Feel free to add more checks if you want to process more formats. */
                if (iData.GetDataPresent(DataFormats.Text))
                {
                    string text = (string)iData.GetData(DataFormats.Text);

                    if (!isUploading)
                        uploadTextData(text);
                }
                else if (iData.GetDataPresent(DataFormats.Bitmap))
                {
                    Bitmap image = (Bitmap)iData.GetData(DataFormats.Bitmap);
                    // do something with it
                }
            }
        }


        private async void uploadTextData(String text) {
            Console.WriteLine(text);
            isUploading = true;
            await api.Push(Properties.Settings.Default.userToken, text);
            isUploading = false;

        }

        public async void fetchInformation()
        {
            while (true) { 
                HttpResponseMessage response = await api.Pull(Properties.Settings.Default.userToken);

                Console.WriteLine(response.Content);
                Thread.Sleep(5000);
            }
        }
   
    }
}
