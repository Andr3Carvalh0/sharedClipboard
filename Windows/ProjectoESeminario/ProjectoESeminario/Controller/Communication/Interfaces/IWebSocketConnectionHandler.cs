using System;

namespace ProjectoESeminario.Controller.Communication.Interfaces
{
    public interface IWebSocketConnectionHandler
    {
        void BeginServerComunication(string user, string deviceID);
        void HandleUpload(string user, string text, string device);
        void HandleUploadMime(string user, byte[] file, string filename, string device);

        void Close();
        bool isAlive();

    }
}
