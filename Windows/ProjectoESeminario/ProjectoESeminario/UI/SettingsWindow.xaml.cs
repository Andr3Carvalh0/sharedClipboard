using MahApps.Metro.Controls;
using System;
using System.ComponentModel;
using System.Windows.Forms;

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

            NotifyIcon icon = new NotifyIcon();
            //Need icon fix
            icon.Icon = new System.Drawing.Icon("ic_launcher.ico");
            icon.ContextMenu = new ContextMenu();
            icon.ContextMenu.MenuItems.Add("Show Window", new EventHandler(showWindow));
            icon.ContextMenu.MenuItems.Add("Exit", new EventHandler(exit));
            icon.Text = "Projecto e Seminario";
            icon.Visible = true;
            this.Show();
        }

        private void showWindow(object sender, EventArgs e)
        {
            this.Show();
        }

        private void exit(object sender, EventArgs e)
        {
            Environment.Exit(1);
        }

        protected override void OnClosing(CancelEventArgs e)
        {
            // setting cancel to true will cancel the close request
            // so the application is not closed
            e.Cancel = true;

            this.Hide();

            base.OnClosing(e);
        }

        private void initHistory()
        {
            foreach(String item in HISTORY_ITEMS) {

                History_Container.Children.Add(new History_Item(item));
            }

        }
    }
}
