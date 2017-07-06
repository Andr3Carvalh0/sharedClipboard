using MahApps.Metro.Controls;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace ProjectoESeminario.UI
{
    /// <summary>
    /// Interaction logic for SettingsWindow.xaml
    /// </summary>
    public partial class SettingsWindow : MetroWindow
    {

        private readonly String[] HISTORY_ITEMS = { "Text", "Contacts", "Images", "Links" };

        public SettingsWindow()
        {
            InitializeComponent();

            ClipboardListener lister = new ClipboardListener();
            initHistory();
        }


        private void initHistory()
        {
            foreach(String item in HISTORY_ITEMS) {

                History_Container.Children.Add(new History_Item(item));
            }

        }
    }
}
