
using ProjectoESeminario.View.History.Interfaces;

namespace ProjectoESeminario.View.History.Category
{
    public class ItemImage : History_Item
    {
        public ItemImage(string name, IHistory history) : base(name, history)
        {
        }

        public override void handleClick(object sender, System.Windows.Input.MouseButtonEventArgs e)
        {
            history.HandleImageCategory();
        }
    }
}
