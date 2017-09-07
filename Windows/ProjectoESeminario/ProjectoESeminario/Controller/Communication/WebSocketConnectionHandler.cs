using Newtonsoft.Json;
using ProjectoESeminario.Controller.Communication.Interfaces;
using ProjectoESeminario.Controller.Communication.Utils;
using System;
using WebSocketSharp;

namespace ProjectoESeminario.Communication
{
    public class WebSocketConnectionHandler : IWebSocketConnectionHandler
    {
        private readonly String socket;
        private readonly String userID;
        private readonly String deviceID;
        private readonly WebSocket ws;
        private readonly Action<String> onUpload;

        public WebSocketConnectionHandler(String socketURL, String userID, String deviceID, Action<String> onReceiveAction, Action<String> onUpload)
        {
            this.socket = socketURL;
            this.userID = userID;
            this.deviceID = deviceID;
            this.ws = new WebSocket(socketURL);
            this.onUpload = onUpload;

            ws.EmitOnPing = true;
            ws.OnMessage += (sender, e) => {
                if (!e.IsPing)
                    onReceiveAction(e.Data);
            };

            ws.Connect();
            BeginServerComunication(userID, deviceID);
        }

        /// <summary>
        /// After establishing the connection to the server we need to perform an "handshake"
        /// The handshake is a JSON message composed by:
        /// action: register
        /// sub: user
        /// id: deviceID
        /// </summary>
        /// <param name="user"></param>
        /// <param name="deviceID"></param>
        public void BeginServerComunication(string user, string deviceID)
        {
            String formattedBody = JsonConvert.SerializeObject(new RegisterJSONWrapper(user, deviceID));
            //can be sync because we are running this on a background thread, 
            //and also we cannot do anything else without this "handshake"
            
             ws.Send(formattedBody);
        }

        /// <summary>
        /// Uploads the text to the server via the websocket connection.
        /// </summary>
        /// <param name="user">The user id(sub)</param>
        /// <param name="text">The string to upload to the server</param>
        /// <param name="device">The device id</param>
        public void HandleUpload(string user, string text, string device)
        {
            String formattedBody = JsonConvert.SerializeObject(new UploadJSONWrapper(user, text, device));
            try { 
                ws.SendAsync(formattedBody, null);
                onUpload.Invoke(text);
            }
            catch (Exception)
            {
                Console.WriteLine();
            }
        }
        /// <summary>
        /// Upload's a file via the websocket connection previously established with the server.
        /// </summary>
        /// <param name="user">The user id(sub)</param>
        /// <param name="file">The file to upload</param>
        /// <param name="filename">The file name</param>
        /// <param name="device">The device id</param>
        public void HandleUploadMime(string user, byte[] file, string filename, string device)
        {
            String formattedBody = JsonConvert.SerializeObject(new UploadMimeJSONWrapper(user, Convert.ToBase64String(file), filename, device));
            try { 
                ws.SendAsync(formattedBody, null);
                onUpload.Invoke(filename);
            }
            catch(Exception)
            {

            }
        }

        /// <summary>
        /// Closes the websocket connection
        /// </summary>
        public void Close()
        {
            if (ws != null && ws.IsAlive)
                ws.Close();
        }

        /// <summary>
        /// Indicates whether or not the websocket connection is up.
        /// </summary>
        /// <returns></returns>
        public bool isAlive()
        {
            return ws.IsAlive;
        }
    }
}
