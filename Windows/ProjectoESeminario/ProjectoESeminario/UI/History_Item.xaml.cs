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
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace ProjectoESeminario.UI
{
    /// <summary>
    /// Interaction logic for History_Item.xaml
    /// </summary>
    public partial class History_Item : UserControl
    {
        private String RESOURCES_PATH = "/Resources/%s.png";

        public History_Item(String name)
        {
            InitializeComponent();

            Title.Content = name;

            Type.Source = new BitmapImage(new Uri(RESOURCES_PATH.Replace("%s", name.ToLower()), UriKind.Relative));


        }
    }
}
