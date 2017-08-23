using ProjectoESeminario.UI.History;
using System.Windows;
using System.Windows.Controls;

namespace ProjectoESeminario.UI
{
    /// <summary>
    /// Interaction logic for History_Detail.xaml
    /// </summary>
    public abstract partial class History_Detail : UserControl
    {
        protected IHistory history;
        protected string name;

        public History_Detail(IHistory history)
        {
            InitializeComponent();
            this.history = history;
            addContent();

        }

        public History_Detail(string name, IHistory history)
        {
            InitializeComponent();
            this.name = name;
            this.history = history;
            Title.Content = name;
            addContent();
        }

        protected abstract void addContent();

        private void Back_Click(object sender, RoutedEventArgs e)
        {
            history.handleBackButton();
        }

        protected void showEmptyView()
        {
            No_Content_Image.Visibility = Visibility.Visible;
            No_Content_Text.Visibility = Visibility.Visible;
        }
    }
}
