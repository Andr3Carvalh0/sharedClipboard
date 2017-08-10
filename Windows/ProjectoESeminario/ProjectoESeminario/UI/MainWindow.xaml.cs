using MahApps.Metro.Controls;
using MahApps.Metro.Controls.Dialogs;
using Projecto.Controllers;
using Projecto.UI;
using ProjectoESeminario.UI;
using System;
using System.Collections.Generic;
using System.Windows;
using System.Windows.Controls;

namespace ProjectoESeminario
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : MetroWindow
    {
        LoginController controller;
        private Dictionary<Type, Action<Exception>> actionsOnException = new Dictionary<Type, Action<Exception>>();
        private Dictionary<System.Net.HttpStatusCode, Action> handleServerResponse = new Dictionary<System.Net.HttpStatusCode, Action>();

        public MainWindow()
        {
            InitializeComponent();

            controller = new LoginController();

            //Setup to handle all the exceptions
            actionsOnException.Add(typeof(WebExceptions), (ex) =>
            {
                Action action = () => { };
                handleServerResponse.TryGetValue(((WebExceptions)ex).simplerError, out action);

                action.Invoke();
            });

            //Setup every action to every possible status code returned by the server
            handleServerResponse.Add(System.Net.HttpStatusCode.Forbidden, async () => await this.ShowMessageAsync("Ops...", "The password isn't valid."));
            handleServerResponse.Add(System.Net.HttpStatusCode.BadRequest, async () =>
            {
                //Show create account dialog
                MessageBoxResult dialogResult = MessageBox.Show("It looks like an account with this email, doesn't exist.\nWould you like to create one", "", MessageBoxButton.OKCancel);
                if (dialogResult == MessageBoxResult.OK)
                {
                    HandleSuccessfulLogin(await controller.HandleCreateAccountAsync(tokenField.Text));
                }
            });
            handleServerResponse.Add(System.Net.HttpStatusCode.InternalServerError, async () =>
            {
                await this.ShowMessageAsync("Ops...", "The server isn't operational. Contact the dumbass that made this app.");
            });
        }

        private void HandleSuccessfulLogin(String sub)
        {
            String GUID = Guid.NewGuid().ToString().ToUpper();

            controller.registerDevice(sub, GUID);

            Properties.Settings.Default.deviceID = GUID;
            Properties.Settings.Default.sub = sub;
            Properties.Settings.Default.Save();
            Properties.Settings.Default.Reload();

            SettingsWindow settings = new SettingsWindow();
            App.Current.MainWindow = settings;
            this.Close();
            settings.Show();
        }

        private void EmailField_TextChanged(object sender, TextChangedEventArgs e)
        {
            NextButton.IsEnabled = HandleProceedButtonVisibility();
        }

        private void PasswordField_PasswordChanged(object sender, RoutedEventArgs e)
        {
            NextButton.IsEnabled = HandleProceedButtonVisibility();
        }

        private bool HandleProceedButtonVisibility()
        {
            return EmailField.Text.Length > 0 && PasswordField.Password.Length > 0;
        }

        private async void NextButton_Click(object sender, RoutedEventArgs e)
        {
            lockControllers(true);

            try
            {
                HandleSuccessfulLogin(await controller.HandleLoginAsync(tokenField.Text));
            }
            catch (Exception ex)
            {
                Action<Exception> actionToPerform = (Exception) => { };

                actionsOnException.TryGetValue(ex.GetType(), out actionToPerform);

                actionToPerform.Invoke(ex);
            }
            finally
            {
                lockControllers(false);
            }
        }

        private void lockControllers(bool showProgressCircle)
        {
            EmailField.IsEnabled = !showProgressCircle;
            PasswordField.IsEnabled = !showProgressCircle;
            NextButton.IsEnabled = !showProgressCircle;
            ProgressCircle.IsActive = showProgressCircle;
        }
    }
}