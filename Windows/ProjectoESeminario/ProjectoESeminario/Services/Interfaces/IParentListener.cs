using ProjectoESeminario.Controller.Communication.Utils;

namespace ProjectoESeminario.Services.Interfaces
{
    public interface IParentListener
    {
        void Logout();
        void StopApplication(string message);
        void OnCopy(string text);
        void OnReceive(string text);
        void OnReceive(ImageEx text);
        void OnCopyMime(string path);
    }
}
