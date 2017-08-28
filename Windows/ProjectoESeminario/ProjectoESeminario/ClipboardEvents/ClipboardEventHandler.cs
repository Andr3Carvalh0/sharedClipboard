using Newtonsoft.Json;
using ProjectoESeminario.Databases;
using ProjectoESeminario.DTOs;
using ProjectoESeminario.Services;
using ProjectoESeminario.State;
using System;
using System.Collections.Generic;
using System.Drawing.Imaging;
using System.IO;
using System.Threading;
using System.Windows.Forms;

namespace ProjectoESeminario.ClipboardEvents
{
    public class ClipboardEventHandler : IClipboardEventHandler
    {
        private readonly String TAG = "Portugal: ClipboardHandler";
        private static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);
        private Dictionary<string, ImageFormat> supported_formats = new Dictionary<string, ImageFormat>();
        private IWebSocketConnectionHandler handler;
        private Dictionary<String, Action<dynamic>> onReceiveActions = new Dictionary<string, Action<dynamic>>();
        private readonly String socketURL;
        private readonly String sub;
        private readonly String id;
        private readonly CancellationTokenSource cts;
        private readonly IClipboardListener clipboard;
        private readonly ClipboardController controller;
        private readonly FileHandler fileHandler;

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
            this.fileHandler = new FileHandler();

            onReceiveActions.Add("report", (s) => handleReport(s));
            onReceiveActions.Add("store", (s) => handleReceived(s));
            onReceiveActions.Add("remove", (s) => handleLogOut(s));
        }

        /// <summary>
        /// Method called when the user copies text on the pc
        /// </summary>
        /// <param name="text">The copied text</param>
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

        /// <summary>
        /// Called when the user copies a file.
        /// This method first verifies if the file is valid for us, and if so upload it to the server
        /// </summary>
        /// <param name="path">the file path</param>
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

        /// <summary>
        /// Called when we receive a message from the server
        /// </summary>
        /// <param name="text">the json received from the server</param>
        public void onReceive(string text)
        {
            var json = JsonConvert.DeserializeObject<dynamic>(text);

            try
            {
                Action<dynamic> tmp;
                onReceiveActions.TryGetValue((String)json.action, out tmp);
                tmp.Invoke(json);
            }
            catch (Exception)
            {

            }
        }

        /// <summary>
        /// Called when we receive a message from the server that contains a image file
        /// </summary>
        /// <param name="json">the json received from the server</param>
        private void handleMime(dynamic json)
        {
            new Thread(() =>
            {
                try
                {
                    String encodedFile = (String)json.content;
                    ImageEx file = fileHandler.store(Convert.FromBase64String(encodedFile), (String) json.filename);

                    if (controller.putValue(file.path, false))
                        clipboard.updateClipboard(file.file);
                }
                finally
                {
                    controller.wake();
                }
            }).Start();
        }

        /// <summary>
        /// Handles the scenario where the message received from the serve only contains text
        /// </summary>
        /// <param name="json">The json received from the server</param>
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

        /// <summary>
        /// Starts the thread that mantains the websocket connection
        /// </summary>
        public void startService()
        {
            Thread workingThread = new Thread(() => WebsocketConnection(cts.Token))
                                                    { IsBackground = true };

            workingThread.Start();
        }

        /// <summary>
        /// Stops the thread that mantains the websocket connection.
        /// </summary>
        public void stopService() { cts.Cancel(); }

        /// <summary>
        /// Handles the websocket connection
        /// </summary>
        /// <param name="cancelToken"></param>
        public void WebsocketConnection(CancellationToken cancelToken)
        {
            handler = new WebSocketConnectionHandler(socketURL, sub, id, (s) => onReceive(s));
            Thread verifyStatusThread = new Thread(() => checkWebSocketStatus(cancelToken))
                                                    { IsBackground = true };

            verifyStatusThread.Start();

            while (true)
            {
                if (cancelToken.IsCancellationRequested)
                {
                    handler.Close();
                    return;
                }
            }
        }
       
        /// <summary>
        /// Constantly checks if the websocket is live
        /// </summary>
        /// <param name="cancelToken"></param>
        public void checkWebSocketStatus(CancellationToken cancelToken)
        {
            while (true)
            {
                if(handler != null && !handler.isAlive())
                    handler = new WebSocketConnectionHandler(socketURL, sub, id, (s) => onReceive(s));

                if (cancelToken.IsCancellationRequested)
                {
                    handler.Close();
                    return;
                }
            }

        }


        /// <summary>
        /// Shows a pop up when a error occures.
        /// </summary>
        /// <param name="json"></param>
        private void handleReport(dynamic json)
        {
            MessageBox.Show((String)json.detail, (String) json.title);
        }

        private void handleLogOut(dynamic json)
        {
            clipboard.logout();
        }

        private void handleReceived(dynamic json) {
            if ((bool)json.isMIME)
            {
                handleMime(json);
                return;
            }

            handleText(json);
        }

    }
}
