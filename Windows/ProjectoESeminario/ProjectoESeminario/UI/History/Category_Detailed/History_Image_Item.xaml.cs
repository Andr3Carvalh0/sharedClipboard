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

namespace ProjectoESeminario.UI.History.Category_Detailed
{
    /// <summary>
    /// Interaction logic for History_Image_Item.xaml
    /// </summary>
    public partial class History_Image_Item : UserControl
    {
        private readonly IHistory history;
        private readonly string path;

        public History_Image_Item(String path, IHistory history)
        {
            InitializeComponent();
            this.history = history;
            this.path = path;
            Image.Source = new BitmapImage(new Uri(path, UriKind.Absolute));
        }

        private void clickEvent(object sender, System.Windows.Input.MouseButtonEventArgs e)
        {
            history.setContent(path);
        }
    }
}
