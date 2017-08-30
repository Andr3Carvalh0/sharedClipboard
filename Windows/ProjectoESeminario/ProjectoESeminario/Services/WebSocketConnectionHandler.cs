using Newtonsoft.Json;
using ProjectoESeminario.Services.Utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using WebSocketSharp;

namespace ProjectoESeminario.Services
{
    public class WebSocketConnectionHandler : IWebSocketConnectionHandler
    {
        private readonly String socket;
        private readonly String userID;
        private readonly String deviceID;
        private WebSocket ws;

        public WebSocketConnectionHandler(String socketURL, String userID, String deviceID, Action<String> onReceiveAction)
        {
            this.socket = socketURL;
            this.userID = userID;
            this.deviceID = deviceID;
            this.ws = new WebSocket(socketURL);

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
        public void HandleUpload(string user, string text)
        {
            String formattedBody = JsonConvert.SerializeObject(new UploadJSONWrapper(user, text));
            ws.SendAsync(formattedBody, null);
        }
        /// <summary>
        /// Upload's a file via the websocket connection previously established with the server.
        /// </summary>
        /// <param name="user">The user id(sub)</param>
        /// <param name="file">The file to upload</param>
        /// <param name="filename">The file name</param>
        public void HandleUploadMime(string user, byte[] file, string filename)
        {
            String formattedBody = JsonConvert.SerializeObject(new UploadMimeJSONWrapper(user, Convert.ToBase64String(file), filename));
            ws.SendAsync(formattedBody, null);
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
