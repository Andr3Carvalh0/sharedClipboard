using ProjectoESeminario.Controller.Communication.Utils;

namespace ProjectoESeminario.Services.Interfaces
{
    public interface IParentListener
    {
        void Logout();
        void StopApplication(string message);
        void OnCopy(string text);
        void OnReceive(string text, int order);
        void OnReceive(ImageEx text, int order);
        void OnCopyMime(string path);
        
        void OnUploadReport(long order);
    }
}
