using ProjectoESeminario.View.History.Interfaces;
using System;
using System.Windows.Controls;
using System.Windows.Media.Imaging;

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
            history.SetContent(path);
        }
    }
}
