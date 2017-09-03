using ProjectoESeminario.View.History.Interfaces;
using System.Threading;

namespace ProjectoESeminario.View.History.Category
{
    public class ItemText : History_Item
    {
        public ItemText(string name, IHistory history) : base(name, history)
        {}

        public override void handleClick(object sender, System.Windows.Input.MouseButtonEventArgs e)
        {
            history.HandleTextCategory(name);
        }
    }
}
