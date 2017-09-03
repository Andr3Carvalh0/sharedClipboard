using ProjectoESeminario.View.History.Interfaces;
using System.Windows.Controls;

namespace ProjectoESeminario.View.History.Category_Detailed
{
    /// <summary>
    /// Interaction logic for History_Text_Item.xaml
    /// </summary>
    public partial class History_Text_Item : UserControl
    {

        private readonly IHistory history;
        private readonly string text;

        public History_Text_Item(string item, IHistory history)
        {
            InitializeComponent();
            Title.Text = Title.Text.ToString().Replace("Text", item);
            this.history = history;
            this.text = item;
        }

        private void ClickEvent(object sender, System.Windows.Input.MouseButtonEventArgs e)
        {
            history.SetContent(text);
        }
    }
}
