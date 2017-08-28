using MahApps.Metro.Controls;
using Microsoft.Win32;
using ProjectoESeminario.Controllers;
using ProjectoESeminario.UI.History;
using ProjectoESeminario.UI.Settings;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Forms;
using System.Windows.Media.Animation;
using System.Drawing;
using ProjectoESeminario.UI.History.Category_Detailed;

namespace ProjectoESeminario.UI
{
    /// <summary>
    /// Interaction logic for SettingsWindow.xaml
    /// </summary>
    public partial class SettingsWindow : MetroWindow, IHistory, ISettings
    {
        private IClipboardListener listener;
        private LinkedList<History_Item> HISTORY_ITEMS;
        private Databases.DatabaseHandler textHandler;
        private Databases.FileHandler fileHandler;
        private String socketURL;
        private readonly String sub;
        private readonly String deviceID;

        //Drawing stuff
        private UIElement gridContent;
        private SettingsControl settingsController;

        //CTOR
        public SettingsWindow() : this(Properties.Settings.Default.sub, Properties.Settings.Default.deviceID)
        {}

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
                listener = new ClipboardListener(socketURL, sub, deviceID, this);

                this.fileHandler = new Databases.FileHandler();
                this.textHandler = new Databases.DatabaseHandler();

                HISTORY_ITEMS = new LinkedList<History_Item>();
                //prep history categories
                HISTORY_ITEMS.AddLast(new ItemText("Text", this));
                HISTORY_ITEMS.AddLast(new ItemText("Contacts", this));
                HISTORY_ITEMS.AddLast(new ItemImage("Images", this));
                HISTORY_ITEMS.AddLast(new ItemText("Links", this));
                initView();

                NotifyIcon icon = new NotifyIcon();
                //Need icon fix
                icon.Icon = new System.Drawing.Icon("ic_launcher.ico");
                icon.ContextMenu = new System.Windows.Forms.ContextMenu();
                icon.ContextMenu.MenuItems.Add("Show Projecto", new EventHandler(showWindow));
                icon.ContextMenu.MenuItems.Add("-");
                icon.ContextMenu.MenuItems.Add("About Projecto", new EventHandler(showAbout));
                icon.ContextMenu.MenuItems.Add("Exit", new EventHandler(exit));
                icon.Text = "Projecto e Seminario";
                icon.Visible = true;
            }
            catch (Exception)
            {
                System.Windows.Forms.MessageBox.Show("Cannot communicate with the server.\nWithout it, this app cannot work.");
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

            Properties.Settings.Default.serviceEnabled = settingsController.getServiceState();
            Properties.Settings.Default.initOnStartup = settingsController.getStartupState();

            base.OnClosing(e);
        }

        private void initView()
        {
            //fresh draw
            if(gridContent == null) { 
                System.Windows.Controls.TabControl tControl = new System.Windows.Controls.TabControl();
                tControl.Height = 503;
                tControl.Width = 362;

                //HistoryPanel
                TabItem tItem = new TabItem();
                tItem.Header = "History";

                StackPanel panel = new StackPanel();
                panel.Name = "History_Container";

                foreach (History_Item item in HISTORY_ITEMS)
                    panel.Children.Add(item);

                tItem.Content = panel;

                //SettingsPanel
                TabItem tItem2 = new TabItem();
                tItem2.Header = "Settings";
                settingsController = new SettingsControl(listener);
                tItem2.Content = settingsController;

                tControl.Items.Add(tItem);
                tControl.Items.Add(tItem2);

                gridContent = tControl;
                Grid_Container.Children.Add(tControl);
                return;
            }

            Grid_Container.Children.Remove(gridContent);
            Grid_Container.Children.Add(gridContent);
        }

        void IHistory.handleTextCategory(string category)
        {
            Grid_Container.Children.Add(new History_Detail_Text(category, this));
            AnimationTrigger.Reload();
        }

        void IHistory.handleImageCategory()
        {
            Grid_Container.Children.Add(new History_Detail_Image(this));
            AnimationTrigger.Reload();
        }

        void IHistory.handleBackButton()
        {
            initView();
            AnimationTrigger.Reload();
        }

        string[] IHistory.fetchContent(string category)
        {
            return textHandler.fetch(category);
        }

        void IHistory.setContent(string text) { }

        void IHistory.setContent(System.Drawing.Image image)
        {
            throw new NotImplementedException();
        }

        public void logout()
        {
            listener.disableService();
            resetSettings();
            fileHandler.destroyAll();
        }

        private void resetSettings()
        {
            Properties.Settings.Default.Reset();
        }
    }
}
