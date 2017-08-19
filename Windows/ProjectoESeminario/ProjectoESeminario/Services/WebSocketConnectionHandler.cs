using Newtonsoft.Json;
using ProjectoESeminario.Services.Utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
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

        public void BeginServerComunication(string user, string deviceID)
        {
            String formattedBody = JsonConvert.SerializeObject(new RegisterJSONWrapper(user, deviceID));
            //can be sync because we are running this on a background thread, 
            //and also we cannot do anything else without this "handshake"
            ws.Send(formattedBody);
        }

        public void HandleUpload(string user, string text)
        {
            String formattedBody = JsonConvert.SerializeObject(new UploadJSONWrapper(user, text));
            ws.SendAsync(formattedBody, null);
        }

        public void HandleUploadMime(string user, byte[] file, string filename)
        {
            throw new NotImplementedException();
        }


        public void Close()
        {
            if (ws != null && ws.IsAlive)
                ws.Close();
        }

        public bool isAlive()
        {
            return ws.IsAlive;
        }
    }
}
