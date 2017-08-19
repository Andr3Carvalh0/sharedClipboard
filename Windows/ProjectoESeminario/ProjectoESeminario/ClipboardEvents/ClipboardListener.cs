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
using ProjectoESeminario.ClipboardEvents;

namespace ProjectoESeminario
{

    public partial class ClipboardListener : Form, IClipboardListener
    {
        private readonly String TAG = "Portugal: ClipboardHandler";
        private static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);
        private readonly IClipboardEventHandler eventHandler;

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

        public ClipboardListener(String socketURL, String userID, String deviceID)
        {
            log.Info(TAG + " ctor");
            this.eventHandler = new ClipboardEventHandler(socketURL, userID, deviceID, this);
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

                    eventHandler.onCopy(text);
                }

                else if (iData.GetDataPresent(DataFormats.FileDrop))
                {
                    log.Debug(TAG + " It could be an format that we support");
                    String image_path = ((string[])iData.GetData(DataFormats.FileDrop))[0];

                    eventHandler.onCopyMime(image_path);
                }
            }
        }

        public void enableService()
        {
            AddClipboardFormatListener(this.Handle);
            eventHandler.startService();   
        }

        public void disableService()
        {
            RemoveClipboardFormatListener(this.Handle);
            eventHandler.stopService();
        }

        public void updateClipboard(string text)
        {
            log.Debug(TAG + "copying text to clipboard");

            Invoke((Action)(() => Clipboard.SetText(text)));
            
        }
    }
}