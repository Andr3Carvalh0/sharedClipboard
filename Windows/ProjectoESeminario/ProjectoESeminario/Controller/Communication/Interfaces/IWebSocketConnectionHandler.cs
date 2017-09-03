using System;

namespace ProjectoESeminario.Controller.Communication.Interfaces
{
    public interface IWebSocketConnectionHandler
    {
        void BeginServerComunication(String user, String deviceID);
        void HandleUpload(String user, String text);
        void HandleUploadMime(String user, byte[] file, String filename);

        void Close();
        bool isAlive();

    }
}
