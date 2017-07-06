using MahApps.Metro.Controls;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Forms;
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

            NotifyIcon icon = new NotifyIcon();
            // icon.Click += new EventHandler(icon_Click);
            //Need icon fix
            icon.Icon = new System.Drawing.Icon("ic_launcher.ico");
            icon.Text = "Projecto e Seminario";
            icon.Visible = true;

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
