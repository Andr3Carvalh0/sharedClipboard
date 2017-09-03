using MahApps.Metro.Controls;
using ProjectoESeminario.Controller;
using ProjectoESeminario.Services;
using ProjectoESeminario.Services.Interfaces;
using ProjectoESeminario.View.About;
using ProjectoESeminario.View.History.Category;
using ProjectoESeminario.View.History.Category_Detailed;
using ProjectoESeminario.View.History.Interfaces;
using ProjectoESeminario.View.Settings;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Forms;

namespace ProjectoESeminario.View
{
    /// <summary>
    /// Interaction logic for SettingsWindow.xaml
    /// </summary>
    public partial class SettingsWindow : MetroWindow, IHistory, ISettingsController
    {
        private IParent listenerController;
        private LinkedList<History_Item> HISTORY_ITEMS;
        private String socketURL;
        private readonly String sub;
        private readonly String deviceID;

        //Drawing stuff
        private UIElement gridContent;
        private PreferencesControl preferencesController;
        private HistoryControl historyController;

        //CTOR
        public SettingsWindow() : this(Properties.Settings.Default.sub, Properties.Settings.Default.deviceID)
        {}

        public SettingsWindow(String sub, String deviceID)
        {
            InitializeComponent();
            
            this.sub = sub;
            this.deviceID = deviceID;
            
            Init();
        }

        private async void Init()
        {
            try
            {
                //Fetch the socket URL
                this.socketURL = await new SettingsController().GetSocketURL();
                
                //Warm up services
                listenerController = new ParentListener(socketURL, sub, deviceID, this);

                HISTORY_ITEMS = new LinkedList<History_Item>();
                //prep history categories
                HISTORY_ITEMS.AddLast(new ItemText(Properties.Resources.CATEGORY_TEXT, this));
                HISTORY_ITEMS.AddLast(new ItemText(Properties.Resources.CATEGORY_CONTACT, this));
                HISTORY_ITEMS.AddLast(new ItemImage(Properties.Resources.CATEGORY_IMAGE, this));
                HISTORY_ITEMS.AddLast(new ItemText(Properties.Resources.CATEGORY_LINK, this));

                InitView();
                InitStatusbar();

            }
            catch (Exception)
            {
                StopApplication(Properties.Resources.SOCKET_ERROR_MESSAGE);
            }
        }

        private void InitView()
        {
            //fresh draw
            if (gridContent == null)
            {
                System.Windows.Controls.TabControl tControl = new System.Windows.Controls.TabControl();
                tControl.Height = 503;
                tControl.Width = 367;

                //HistoryPanel
                TabItem tItem = new TabItem();
                tItem.Header = Properties.Resources.HISTORY_PANEL;

                historyController = new HistoryControl(HISTORY_ITEMS);
                tItem.Content = historyController;

                //SettingsPanel
                TabItem tItem2 = new TabItem();
                tItem2.Header = Properties.Resources.SETTINGS_PANEL;
                preferencesController = new PreferencesControl(listenerController);
                tItem2.Content = preferencesController;

                tControl.Items.Add(tItem);
                tControl.Items.Add(tItem2);

                gridContent = tControl;
                Grid_Container.Children.Add(tControl);
                return;
            }

            Grid_Container.Children.Remove(gridContent);
            Grid_Container.Children.Add(gridContent);
        }

        private void InitStatusbar()
        {
            NotifyIcon icon = new NotifyIcon();
            //Need icon fix
            icon.Icon = Properties.Resources.ic_launcher;
            icon.ContextMenu = new System.Windows.Forms.ContextMenu();
            icon.ContextMenu.MenuItems.Add(Properties.Resources.STATUS_SHOW, new EventHandler(ShowWindow));
            icon.ContextMenu.MenuItems.Add("-");
            icon.ContextMenu.MenuItems.Add(Properties.Resources.STATUS_ABOUT, new EventHandler(ShowAboutWindow));
            icon.ContextMenu.MenuItems.Add(Properties.Resources.STATUS_EXIT, new EventHandler(Exit));
            icon.Text = Properties.Resources.APP_NAME;
            icon.Visible = true;
        }

        private void ShowAboutWindow(object sender, EventArgs e)
        {
            AboutWindow about = new AboutWindow();
            App.Current.MainWindow = about;
            about.Show();
        }

        private void ShowWindow(object sender, EventArgs e)
        {
            this.Show();
        }

        private void Exit(object sender, EventArgs e)
        {
            Environment.Exit(1);
        }

        protected override void OnClosing(CancelEventArgs e)
        {
            // setting cancel to true will cancel the close request
            // so the application is not closed
            e.Cancel = true;

            this.Hide();

            Properties.Settings.Default.serviceEnabled = preferencesController.getServiceState();
            Properties.Settings.Default.initOnStartup = preferencesController.getStartupState();

            base.OnClosing(e);
        }

        public void Logout()
        {
            Properties.Settings.Default.Reset();
            StopApplication(Properties.Resources.DEVICE_REMOVED);
        }

        public void StopApplication(string message)
        {
            System.Windows.Forms.MessageBox.Show(message);
            Exit(null, null);
        }

        /// <summary>
        /// Switches to the detailed view of the category
        /// </summary>
        /// <param name="category"></param>
        void IHistory.HandleTextCategory(string category)
        {
            Grid_Container.Children.Add(new History_Detail_Text(category, this));
            AnimationTrigger.Reload();
        }
        
        /// <summary>
        /// Switches to the detailed view of the category images
        /// </summary>
        /// <param name="category"></param>
        void IHistory.HandleImageCategory()
        {
            Grid_Container.Children.Add(new History_Detail_Image(this));
            AnimationTrigger.Reload();
        }

        /// <summary>
        /// Handles the back button press, to return to the main history view
        /// </summary>
        void IHistory.HandleBackButton()
        {
            InitView();
            AnimationTrigger.Reload();
        }

        /// <summary>
        /// Sets the clip on the history panel as the new clipboard value
        /// without, making it the last value copied.
        /// </summary>
        /// <param name="text"></param>
        void IHistory.SetContent(string text) {
            listenerController.UpdateClipboard(text);
        }
        
        /// <summary>
        /// Sets the clip on the history panel as the new clipboard value
        /// without, making it the last value copied.
        /// </summary>
        /// <param name="image"></param>
        void IHistory.SetContent(System.Drawing.Image image)
        {
            listenerController.UpdateClipboard(image);
        }

        /// <summary>
        /// Returns all the content of a category
        /// </summary>
        /// <param name="category"></param>
        /// <returns></returns>
        string[] IHistory.FetchContent(string category)
        {
            return listenerController.Pull(category);
        }

    }
}
