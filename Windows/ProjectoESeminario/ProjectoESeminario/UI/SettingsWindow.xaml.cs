using MahApps.Metro.Controls;
using Microsoft.Win32;
using ProjectoESeminario.Controllers;
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
        private IClipboardListener listener;
        private readonly String[] HISTORY_ITEMS = { "Text", "Contacts", "Images", "Links" };

        private String socketURL;
        private readonly String sub;
        private readonly String deviceID;

        public SettingsWindow() : this(Properties.Settings.Default.sub, Properties.Settings.Default.deviceID)
        {
        }
        public SettingsWindow(String sub, String deviceID)
        {
            InitializeComponent();

            this.sub = sub;
            this.deviceID = deviceID;

            string folder = Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData);
            init();
        }

        private async void init()
        {


            try
            {
                this.socketURL = await new SettingsController().GetSocketURL();
                listener = new ClipboardListener(socketURL, sub, deviceID);
                initHistory();
                initSettings();

                NotifyIcon icon = new NotifyIcon();
                //Need icon fix
                icon.Icon = new System.Drawing.Icon("ic_launcher.ico");
                icon.ContextMenu = new ContextMenu();
                icon.ContextMenu.MenuItems.Add("Show Projecto", new EventHandler(showWindow));
                icon.ContextMenu.MenuItems.Add("-");
                icon.ContextMenu.MenuItems.Add("About Projecto", new EventHandler(showAbout));
                icon.ContextMenu.MenuItems.Add("Exit", new EventHandler(exit));
                icon.Text = "Projecto e Seminario";
                icon.Visible = true;
            }
            catch (Exception)
            {
                MessageBox.Show("Cannot communicate with the server.\nWithout it, this app cannot work.");
                exit(null, null);
            }
        }

        private void showAbout(object sender, EventArgs e)
        {
            AboutWindow about = new AboutWindow();
            App.Current.MainWindow = about;
            about.Show();
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

            if (Service_Enabled.IsChecked.Value)
                listener.enableService();

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
