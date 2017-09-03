using System.Drawing;

namespace ProjectoESeminario.Services.Interfaces
{
    public interface IParent
    {
        void EnableService();
        void DisableService();

        void UpdateClipboard(Image image);
        void UpdateClipboard(string text);
        string[] Pull(string category);
    }
}
