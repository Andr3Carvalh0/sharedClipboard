using MahApps.Metro.Controls;
using System;

namespace ProjectoESeminario.View.About
{
    /// <summary>
    /// Interaction logic for AboutWindow.xaml
    /// </summary>
    public partial class AboutWindow : MetroWindow
    {
        private readonly String[] libs = new String[] { "GoogleAPI", "log4net", "MahApps", "Newtonsoft", "Websocket-sharp", "MonitorEx" };
        private readonly String[] libs_description = new String[] {
            "The Google APIs Client Library is a runtime client for working with Google services.",
            "The Apache Software Foundation log4net Logging Framework.",
            "A toolkit for creating Metro / Modern UI styled WPF apps.",
            "Json.NET is a popular high-performance JSON framework for .NET",
            "A C# implementation of the WebSocket protocol client and server.",
            "Extension to the Monitor class in order to support Lampson and Redell monitors with multiple condition variables."
        };

        public AboutWindow()
        {
            InitializeComponent();

            for(int i = 0; i < libs.Length; i++)
            {
                container.Children.Add(new About_Item(libs[i], libs_description[i]));

            }
        }
    }
}
