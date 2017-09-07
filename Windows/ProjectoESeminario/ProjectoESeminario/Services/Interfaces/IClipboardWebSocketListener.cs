using System;

namespace ProjectoESeminario.Services.Interfaces
{
    public interface IClipboardWebSocketListener
    {
   
        void HandleUpload(string user, string text, string device);
        void HandleUploadMime(string user, byte[] file, string filename, string device);
        void Start();
        void Stop();
    }
}
