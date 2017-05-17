using Projecto.UI;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Projecto
{
    static class EntryPoint
    {
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);

            if(Properties.Settings.Default.userToken == 0) { 
                Application.Run(new LoginWindow());
            }
            else
            {
                Application.Run(new SettingsWindow());
            }
        }

        public static void switchToSettings()
        {
            Application.Restart();
        }
    }
}
