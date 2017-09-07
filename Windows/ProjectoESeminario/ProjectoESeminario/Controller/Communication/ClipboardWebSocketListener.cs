using Newtonsoft.Json;
using ProjectoESeminario.Communication;
using ProjectoESeminario.Controller.Communication.Interfaces;
using ProjectoESeminario.Controller.Communication.Utils;
using ProjectoESeminario.Services.Interfaces;
using System;
using System.Collections.Generic;
using System.Threading;
using System.Windows.Forms;

namespace ProjectoESeminario.Services
{
    public class ClipboardWebSocketListener : IClipboardWebSocketListener
    {
        private readonly String TAG = "Portugal: ClipboardHandler";
        private static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);

        private IWebSocketConnectionHandler handler;
        private readonly Dictionary<String, Action<dynamic>> onReceiveActions = new Dictionary<string, Action<dynamic>>();
        private readonly String socketURL;
        private readonly String sub;
        private readonly String id;
        private CancellationTokenSource cts;
        private readonly IParentListener parent;

        public ClipboardWebSocketListener(String socketURL, String userID, String deviceID, IParentListener parent)
        {
            this.socketURL = socketURL;
            this.sub = userID;
            this.id = deviceID;
            this.cts = new CancellationTokenSource();
            this.parent = parent;

            onReceiveActions.Add("report", (s) => HandleReport(s));
            onReceiveActions.Add("store", (s) => HandleReceived(s));
            onReceiveActions.Add("remove", (s) => HandleLogOut(s));
        }

        /// <summary>
        /// Called when we receive a message from the server
        /// </summary>
        /// <param name="text">the json received from the server</param>
        private void OnReceive(string text)
        {
            var json = JsonConvert.DeserializeObject<dynamic>(text);

            try
            {
                onReceiveActions.TryGetValue((String)json.action, out var tmp);
                tmp.Invoke(json);
            }
            catch (Exception)
            {

            }
        }

        /// <summary>
        /// Shows a pop up when a error occures.
        /// </summary>
        /// <param name="json"></param>
        private void HandleReport(dynamic json)
        {
            if((String)json.error != null) { 
                MessageBox.Show((String)json.detail, (String)json.title);
                return;
            }

            //The server only reports in 2 cases...
            //Or the operation failed, or succeded.
            //If we reach this point, we have succeded, so save the order number
            long request_order = (long) json.data.order;
            parent.OnUploadReport(request_order);
  
        }

        /// <summary>
        /// Handles the logout request
        /// </summary>
        /// <param name="json"></param>
        private void HandleLogOut(dynamic json)
        {
            parent.Logout();
        }

        /// <summary>
        /// Handles the logic when we receive a message from the server
        /// </summary>
        /// <param name="json"></param>
        private void HandleReceived(dynamic json)
        {
            if ((bool)json.isMIME)
            {
                HandleMime(json);
                return;
            }

            HandleText(json);
        }

        /// <summary>
        /// Handles the scenario where the message received from the serve only contains text
        /// </summary>
        /// <param name="json">The json received from the server</param>
        private void HandleText(dynamic json)
        {
            parent.OnReceive((String)json.content, (int) json.order);
        }

        /// <summary>
        /// Called when we receive a message from the server that contains a image file
        /// </summary>
        /// <param name="json">the json received from the server</param>
        private void HandleMime(dynamic json)
        {
            parent.OnReceive(ImageDecoder.decode((String)json.content, (String)json.filename), (int)json.order);
        }

        /// <summary>
        /// Handles the websocket connection
        /// </summary>
        /// <param name="cancelToken"></param>
        public void WebsocketConnection(CancellationToken cancelToken)
        {
            handler = new WebSocketConnectionHandler(socketURL, sub, id, OnReceive, OnUpload);
            Thread verifyStatusThread = new Thread(() => CheckWebSocketStatus(cancelToken))
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
        public void CheckWebSocketStatus(CancellationToken cancelToken)
        {
            int max_tries = 3;
            int tries = 0;
            while (true)
            {
                if(handler != null && !handler.isAlive() && max_tries > tries)
                {
                    try
                    {
                        handler = new WebSocketConnectionHandler(socketURL, sub, id, OnReceive, OnUpload);
                        tries = 0;
                    }
                    catch (Exception)
                    {
                        tries++;
                        //Try again in 3s
                        Thread.Sleep(3000);
                    }
                }
                
                if (cancelToken.IsCancellationRequested)
                {
                    handler.Close();
                    return;
                }

                //Notify the user that we cannot continue to run.
                if(max_tries <= tries)
                    parent.StopApplication(Properties.Resources.SOCKET_ERROR_MESSAGE);
            }

        }

        /// <summary>
        /// Upload information to the server
        /// </summary>
        /// <param name="user">user id</param>
        /// <param name="text">the text copied</param>
        public void HandleUpload(string user, string text)
        {
            handler.HandleUpload(user, text);
        }

        /// <summary>
        /// Handles the file upload
        /// </summary>
        /// <param name="user">user id</param>
        /// <param name="file">the file</param>
        /// <param name="filename">the name of the file</param>
        public void HandleUploadMime(string user, byte[] file, string filename)
        {
            handler.HandleUploadMime(user, file, filename);
        }

        /// <summary>
        /// Starts the thread that mantains the websocket connection
        /// </summary>
        public void Start()
        {
            Thread workingThread = new Thread(() => WebsocketConnection(cts.Token))
            { IsBackground = true };

            workingThread.Start();
        }


        public void OnUpload(String text)
        {
            

        }
        /// <summary>
        /// Stops the thread that mantains the websocket connection.
        /// </summary>
        public void Stop() {
            cts.Cancel();
            cts = new CancellationTokenSource();
        }
    }
}
