using Newtonsoft.Json;
using ProjectoESeminario.Services;
using ProjectoESeminario.State;
using System;
using System.Collections.Generic;
using System.Drawing.Imaging;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using WebSocketSharp;

namespace ProjectoESeminario.ClipboardEvents
{
    public class ClipboardEventHandler : IClipboardEventHandler
    {
        private readonly String TAG = "Portugal: ClipboardHandler";
        private static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);
        private Dictionary<string, ImageFormat> supported_formats = new Dictionary<string, ImageFormat>();
        private IWebSocketConnectionHandler handler;

        private readonly String socketURL;
        private readonly String sub;
        private readonly String id;
        private readonly CancellationTokenSource cts;
        private readonly IClipboardListener clipboard;
        private readonly ClipboardController controller;

        public ClipboardEventHandler(String socketURL, String userID, String deviceID, IClipboardListener clipboard)
        {
            this.socketURL = socketURL;
            this.sub = userID;
            this.id = deviceID;
            this.clipboard = clipboard;
            this.supported_formats.Add("png", ImageFormat.Png);
            this.supported_formats.Add("jpg", ImageFormat.Jpeg);
            this.cts = new CancellationTokenSource();
            this.controller = ClipboardControllerFactory.getSingleton("Welcome!");
        }
        /// <summary>
        /// Method called when the user copies text on the pc
        /// </summary>
        /// <param name="text"></param>
        public void onCopy(string text)
        {
            new Thread(() =>
            {
                try
                {
                    if (controller.putValue(text, true))
                    {
                        if (handler.isAlive())
                        {
                            handler.HandleUpload(sub, text);
                            return;
                        }
                        
                    }
                }
                finally
                {
                    controller.wake();
                }

            }).Start();
        }

        public void onCopyMime(string path)
        {
            new Thread(() =>
            {
                string[] tmp = path.Split('\\');
                string[] file_format = tmp[tmp.Length - 1].Split('.');

                ImageFormat format = null;

                if (supported_formats.TryGetValue(file_format[file_format.Length - 1], out format))
                {
                    log.Debug(TAG + " Upload file to server");
                    
                    byte[] image = File.ReadAllBytes(path);

                    try
                    {
                        if (controller.putValue(path, false)) { 
                            if (handler.isAlive())
                            {
                                handler.HandleUploadMime(sub, image, tmp[tmp.Length - 1]);
                                return;
                            }
                        }
                    }
                    finally
                    {
                        controller.wake();
                    }
                }
            }).Start();


        }

        public void onReceive(string text)
        {
            var json = JsonConvert.DeserializeObject<dynamic>(text);

            if ((bool)json.isMIME) { 
                handleMime(json);
                return;
            }

            handleText(json);
        }

        private void handleMime(dynamic json)
        {

        }

        private void handleText(dynamic json)
        {
            new Thread(() =>
            {
                try
                {
                    String text = (String)json.content;
                    if (controller.putValue(text, true))
                        clipboard.updateClipboard(text);
                }
                finally
                {
                    controller.wake();
                }
            }).Start();
        }

        public void startService()
        {
            Thread workingThread = new Thread(() => WebsocketConnection(cts.Token))
            { IsBackground = true };

            workingThread.Start();
        }

        public void stopService() { cts.Cancel(); }

        public void WebsocketConnection(CancellationToken cancelToken)
        {
            handler = new WebSocketConnectionHandler(socketURL, sub, id, (s) => onReceive(s));
            int times = 0;
            while (true)
            {
                if (cancelToken.IsCancellationRequested || times == 3)
                {
                    handler.Close();
                    return;
                }

                if (!handler.isAlive())
                {
                    times++;
                    handler = new WebSocketConnectionHandler(socketURL, sub, id, (s) => onReceive(s));
                }
            }
        }
    }
}
