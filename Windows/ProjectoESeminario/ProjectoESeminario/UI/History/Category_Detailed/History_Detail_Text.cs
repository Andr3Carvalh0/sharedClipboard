using ProjectoESeminario.UI.History.Category_Detailed;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ProjectoESeminario.UI.History
{
    public class History_Detail_Text : History_Detail
    {
        public History_Detail_Text(string name, IHistory history) : base(name, history){}

        protected override void addContent()
        {
            string[] content = history.fetchContent(name.ToLower());

            if (content == null) { 
                showEmptyView();
                return;
            }

            foreach (string item in content)
            {
                Container.Children.Add(new History_Text_Item(item));
            }
        }
    }
}
