using ProjectoESeminario.View.History.Interfaces;
using System.Windows;
using System.Windows.Controls;

namespace ProjectoESeminario.UI.History.Category_Detailed
{
    /// <summary>
    /// Interaction logic for History_Detail.xaml
    /// </summary>
    public abstract partial class History_Detail : UserControl
    {
        protected IHistory history;
        protected string name;

        public History_Detail(string name, IHistory history)
        {
            InitializeComponent();
            this.name = name;
            this.history = history;
            Title.Content = name;
            AddContent();
        }

        protected abstract void AddContent();

        private void Back_Click(object sender, RoutedEventArgs e)
        {
            history.HandleBackButton();
        }

        protected void showEmptyView()
        {
            No_Content_Image.Visibility = Visibility.Visible;
            No_Content_Text.Visibility = Visibility.Visible;
        }
    }
}
