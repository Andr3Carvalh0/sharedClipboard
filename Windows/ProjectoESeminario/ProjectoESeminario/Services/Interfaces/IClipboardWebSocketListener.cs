using System;

namespace ProjectoESeminario.Services.Interfaces
{
    public interface IClipboardWebSocketListener
    {
   
        void HandleUpload(string user, string text);
        void HandleUploadMime(string user, byte[] file, string filename);
        void Start();
        void Stop();
    }
}
