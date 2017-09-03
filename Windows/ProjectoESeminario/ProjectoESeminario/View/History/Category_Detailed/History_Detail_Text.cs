using ProjectoESeminario.UI.History.Category_Detailed;
using ProjectoESeminario.View.History.Interfaces;
using System.Linq;

namespace ProjectoESeminario.View.History.Category_Detailed
{
    public class History_Detail_Text : History_Detail
    {
        public History_Detail_Text(string name, IHistory history) : base(name, history){}

        protected override void AddContent()
        {
            string[] content = history.FetchContent(name.ToLower());

            if (content.Count() == 0) { 
                showEmptyView();
                return;
            }

            foreach (string item in content)
            {
                Container.Children.Add(new History_Text_Item(item, history));
            }
        }
    }
}
