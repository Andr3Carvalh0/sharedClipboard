using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Linq;
using System.Threading.Tasks;
using System.Windows;

namespace ProjectoESeminario
{
    public partial class App : Application
    {
        protected override void OnStartup(StartupEventArgs e)
        {
            base.OnStartup(e);

            if (ProjectoESeminario.Properties.Settings.Default.userToken == 0) {
                StartupUri = new Uri("UI/MainWindow.xaml", UriKind.Relative);
            }else{
                StartupUri = new Uri("UI/SettingsWindow.xaml", UriKind.Relative);
            }
        }
    }
}
