using System.Drawing;

namespace ProjectoESeminario.Services.Interfaces
{
    public interface IParent
    {
        void EnableService();
        void DisableService();

        void UpdateClipboard(Image image, string path);
        void UpdateClipboard(string text);
        string[] Pull(string category);
        string[] Pull();

        void SaveOrder();
    }
}
