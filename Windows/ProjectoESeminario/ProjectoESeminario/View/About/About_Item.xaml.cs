using System;
using System.Windows.Controls;

namespace ProjectoESeminario.View.About
{
    /// <summary>
    /// Interaction logic for About_Item.xaml
    /// </summary>
    public partial class About_Item : UserControl
    {
        public About_Item(String title, String description)
        {
            InitializeComponent();
            Title.Content = Title.Content.ToString().Replace("Text", title);
            Description.Text = description;

        }
    }
}
