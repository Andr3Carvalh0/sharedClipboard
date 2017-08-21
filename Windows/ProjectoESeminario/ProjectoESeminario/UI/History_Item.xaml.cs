using System;
using System.Windows.Controls;
using System.Windows.Media.Imaging;
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
