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
        private static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);
        private readonly String TAG = "Portugal: Main";

        protected override void OnStartup(StartupEventArgs e)
        {
            base.OnStartup(e);

            if (ProjectoESeminario.Properties.Settings.Default.sub.Equals("")) {
                log.Debug(TAG + " loading loginWindow");
                StartupUri = new Uri("UI/MainWindow.xaml", UriKind.Relative);
            }else{
                log.Debug(TAG + " loading settingsWindow");
                StartupUri = new Uri("UI/SettingsWindow.xaml", UriKind.Relative);
            }
        }
    }
}
