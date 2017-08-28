using Microsoft.Win32;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace ProjectoESeminario.UI.Settings
{
    /// <summary>
    /// Interaction logic for SettingsControl.xaml
    /// </summary>
    public partial class SettingsControl : UserControl
    {
        private readonly IClipboardListener listener;

        public SettingsControl(IClipboardListener listener)
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
                listener.enableService();

            StartUP.IsChecked = Properties.Settings.Default.initOnStartup;
            StartUP.IsCheckedChanged += onStartupChanged;
        }

        private void onServiceChanged(object sender, System.EventArgs e)
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
