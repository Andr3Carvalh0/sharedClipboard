using ProjectoESeminario.UI.History;
using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media.Imaging;
namespace ProjectoESeminario.UI
{
    /// <summary>
    /// Interaction logic for History_Item.xaml
    /// </summary>
    public abstract partial class History_Item : UserControl
    {
        private String RESOURCES_PATH = "/Resources/%s.png";
        protected IHistory history;
        protected string name;

        public History_Item(string name, IHistory history)
        {
            InitializeComponent();
            Title.Content = name;
            this.name = name;
            this.history = history;
            Type.Source = new BitmapImage(new Uri(RESOURCES_PATH.Replace("%s", name.ToLower()), UriKind.Relative));
            
        }

        public abstract void handleClick(object sender, System.Windows.Input.MouseButtonEventArgs e);
    }
}
