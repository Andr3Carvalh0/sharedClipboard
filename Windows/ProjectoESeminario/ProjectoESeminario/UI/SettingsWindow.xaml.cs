using MahApps.Metro.Controls;
using Microsoft.Win32;
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
        private readonly IClipboardListener listener;
        private readonly String[] HISTORY_ITEMS = { "Text", "Contacts", "Images", "Links" };

        public SettingsWindow()
        {
            InitializeComponent();

            listener = new ClipboardListener();
            initHistory();
            initSettings();

            NotifyIcon icon = new NotifyIcon();
            //Need icon fix
            icon.Icon = new System.Drawing.Icon("ic_launcher.ico");
            icon.ContextMenu = new ContextMenu();
            icon.ContextMenu.MenuItems.Add("Show Window", new EventHandler(showWindow));
            icon.ContextMenu.MenuItems.Add("Exit", new EventHandler(exit));
            icon.Text = "Projecto e Seminario";
            icon.Visible = true;

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

            Properties.Settings.Default.serviceEnabled = Service_Enabled.IsChecked.Value;
            Properties.Settings.Default.initOnStartup = StartUP.IsChecked.Value;

            base.OnClosing(e);
        }

        private void initHistory()
        {
            foreach(String item in HISTORY_ITEMS) {

                History_Container.Children.Add(new History_Item(item));
            }

        }

        private void initSettings()
        {
            Service_Enabled.IsChecked = Properties.Settings.Default.serviceEnabled;
            Service_Enabled.IsCheckedChanged += onServiceChanged;

            StartUP.IsChecked = Properties.Settings.Default.initOnStartup;
            StartUP.IsCheckedChanged += onStartupChanged;
        }

        private void onServiceChanged (object sender, System.EventArgs e)
        {
            if (Service_Enabled.IsChecked.Value)
            {
                listener.enableService();
                return;
            }

            listener.disableService();
        }

        // Why do it this way?
        // To start the clipboard listening we start a hidden windows so we can add a valid id 
        // to the clipboard listener API
        // So to do that we need to have the application running since its the only valid way to launch a window.
        // The problem is that we cant launch applications from a windows service.
        // So we use add our App to the registry.
        private void onStartupChanged(object sender, System.EventArgs e)
        {
            RegistryKey rk = Registry.CurrentUser.OpenSubKey("SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run", true);

            if (StartUP.IsChecked.Value) {
                rk.SetValue("ProjectoESeminario", Application.ExecutablePath.ToString());
                return;
            }

            rk.DeleteValue("ProjectoESeminario", false);
        }
    }
}
