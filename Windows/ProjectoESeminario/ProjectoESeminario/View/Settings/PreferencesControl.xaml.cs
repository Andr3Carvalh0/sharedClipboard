using Microsoft.Win32;
using ProjectoESeminario.Services.Interfaces;
using System;
using System.Windows.Controls;

namespace ProjectoESeminario.View.Settings
{
    /// <summary>
    /// Interaction logic for SettingsControl.xaml
    /// </summary>
    public partial class PreferencesControl : UserControl
    {
        private readonly IParent listener;

        public PreferencesControl(IParent listener)
        {
            InitializeComponent();
            this.listener = listener;
            initSettings();
        }

        private void initSettings()
        {
            Service_Enabled.IsChecked = Properties.Settings.Default.serviceEnabled;
            Service_Enabled.IsCheckedChanged += onServiceChanged;

            if (Service_Enabled.IsChecked.Value)
                listener.EnableService();

            StartUP.IsChecked = Properties.Settings.Default.initOnStartup;
            StartUP.IsCheckedChanged += onStartupChanged;
        }

        private void onServiceChanged(object sender, System.EventArgs e)
        {
            if (Service_Enabled.IsChecked.Value)
            {
                listener.EnableService();
                return;
            }

            listener.DisableService();
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

            if (StartUP.IsChecked.Value)
            {
                rk.SetValue("ProjectoESeminario", System.Windows.Forms.Application.ExecutablePath.ToString());
                return;
            }

            rk.DeleteValue("ProjectoESeminario", false);
        }


        public Boolean getServiceState()
        {
            return Service_Enabled.IsChecked.Value;
        }

        public Boolean getStartupState()
        {
            return StartUP.IsChecked.Value;
        }
        
    }
}
