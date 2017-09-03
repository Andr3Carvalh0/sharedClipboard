using ProjectoESeminario.View.History.Category;
using System.Collections.Generic;
using System.Windows.Controls;

namespace ProjectoESeminario.View.Settings
{
    /// <summary>
    /// Interaction logic for HistoryControl.xaml
    /// </summary>
    public partial class HistoryControl : UserControl
    {
        public HistoryControl(LinkedList<History_Item> items)
        {
            InitializeComponent();

            foreach (History_Item item in items)
                panel.Children.Add(item);
        }
    }
}
