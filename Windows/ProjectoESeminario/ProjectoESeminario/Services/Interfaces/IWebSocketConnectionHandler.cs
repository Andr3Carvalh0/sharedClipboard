using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ProjectoESeminario.Services
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
