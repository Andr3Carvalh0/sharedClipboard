using System.Drawing;

namespace ProjectoESeminario.View.History.Interfaces
{
    public interface IHistory
    {
        void HandleTextCategory(string category);

        void HandleImageCategory();

        void HandleBackButton();

        string[] FetchContent(string category);

        void SetContent(string text);

        void SetContent(Image image);
    }
}
