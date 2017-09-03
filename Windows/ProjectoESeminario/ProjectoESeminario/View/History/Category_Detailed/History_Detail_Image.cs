using ProjectoESeminario.UI.History.Category_Detailed;
using ProjectoESeminario.View.History.Interfaces;

namespace ProjectoESeminario.View.History.Category_Detailed
{
    public class History_Detail_Image : History_Detail
    {
        public History_Detail_Image(IHistory history) : base(Properties.Resources.CATEGORY_IMAGE, history)
        {
        }

        protected override void AddContent()
        {
            string[] content = history.FetchContent();

            if (content.Length == 0)
            {
                showEmptyView();
                return;
            }

            foreach (string item in content)
            {
                Container.Children.Add(new History_Image_Item(item, history));
            }
        }
    }
}
