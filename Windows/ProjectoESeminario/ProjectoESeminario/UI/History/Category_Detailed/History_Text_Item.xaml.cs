using System.Windows.Controls;

namespace ProjectoESeminario.UI.History.Category_Detailed
{
    /// <summary>
    /// Interaction logic for History_Text_Item.xaml
    /// </summary>
    public partial class History_Text_Item : UserControl
    {
        public History_Text_Item(string item)
        {
            InitializeComponent();
            Title.Content = Title.Content.ToString().Replace("Text", item);
        }
    }
}
