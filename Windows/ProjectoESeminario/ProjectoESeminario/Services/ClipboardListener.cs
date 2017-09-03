using System;
using System.Runtime.InteropServices;
using System.Windows.Forms;
using System.Drawing;
using ProjectoESeminario.Services.Interfaces;

namespace ProjectoESeminario.Services
{
    public partial class ClipboardListener : Form, IClipboardListener
    {
        private readonly String TAG = "Portugal: ClipboardHandler";
        private static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);
        private readonly IParentListener parent;

        public ClipboardListener(IParentListener parent)
        {
            this.parent = parent;
        }

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

        /// <summary>
        /// Called when the windows receives a "message"
        /// </summary>
        /// <param name="m"></param>
        protected override void WndProc(ref Message m)
        {
            base.WndProc(ref m);

            if (m.Msg == WM_CLIPBOARDUPDATE)
            {
                log.Debug(TAG + " Changes where detected in the clipboard");
                IDataObject iData = Clipboard.GetDataObject(); // Clipboard's data.

                /* Depending on the clipboard's current data format we can process the data differently. */
                if (iData.GetDataPresent(DataFormats.Text)) {
                    HandleText(iData);
                    return;
                }

                if (iData.GetDataPresent(DataFormats.FileDrop))
                    HandleFile(iData);
            }
        }

        /// <summary>
        /// Sets a value to the system clipboard
        /// </summary>
        /// <param name="text">the value to set, as the new value to the clipboard</param>
        public void UpdateClipboard(string text)
        {
            log.Debug(TAG + "copying text to clipboard");

            Invoke((Action)(() => Clipboard.SetText(text)));
        }
        
        /// <summary>
        /// Sets a image to the system clipboard
        /// </summary>
        /// <param name="image">the value to set, as the new value to the clipboard</param>
        public void UpdateClipboard(Image image)
        {
            log.Debug(TAG + "copying image to clipboard");

            Invoke((Action)(() => Clipboard.SetImage(image)));
        }

        /// <summary>
        /// Called when the user copies a new value
        /// </summary>
        /// <param name="iData"></param>
        private void HandleText(IDataObject iData)
        {
            log.Debug(TAG + " It was text");
            string text = (string)iData.GetData(DataFormats.Text);
            parent.OnCopy(text);
        }

        /// <summary>
        /// Called when the user copies a file
        /// </summary>
        /// <param name="iData"></param>
        private void HandleFile(IDataObject iData)
        {
            log.Debug(TAG + " It could be an format that we support");
            String image_path = ((string[])iData.GetData(DataFormats.FileDrop))[0];
            parent.OnCopyMime(image_path);
        }

        /// <summary>
        /// Starts the listener
        /// </summary>
        public void Start()
        {
            AddClipboardFormatListener(this.Handle);
        }

        /// <summary>
        /// Stops the listener
        /// </summary>
        public void Stop()
        {
            RemoveClipboardFormatListener(this.Handle);
        }
    }
}
