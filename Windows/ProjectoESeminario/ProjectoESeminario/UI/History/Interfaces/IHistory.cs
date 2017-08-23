using System.Collections.Generic;
using System.Drawing;

namespace ProjectoESeminario.UI.History
{
    public interface IHistory
    {
        void handleTextCategory(string category);

        void handleImageCategory();

        void handleBackButton();

        string[] fetchContent(string category);

        void setContent(string text);

        void setContent(Image image);
    }
}
