using Google.Apis.Auth.OAuth2;
using MahApps.Metro.Controls;
using MahApps.Metro.Controls.Dialogs;
using ProjectoESeminario.Controller;
using ProjectoESeminario.Exceptions;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Threading;
using System.Windows;

namespace ProjectoESeminario.View
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class LoginWindow : MetroWindow
    {
        readonly LoginController controller;
        private readonly Dictionary<Type, Action<Exception>> actionsOnException = new Dictionary<Type, Action<Exception>>();
        private readonly Dictionary<System.Net.HttpStatusCode, Action> handleServerResponse = new Dictionary<System.Net.HttpStatusCode, Action>();

        private readonly ClientSecrets cs = new ClientSecrets() { ClientId = ConfigurationManager.AppSettings["Google_ID"], ClientSecret = ConfigurationManager.AppSettings["Google_Secret"] };

        private String token;

        public LoginWindow()
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
            handleServerResponse.Add(System.Net.HttpStatusCode.Forbidden, async () => await this.ShowMessageAsync(Properties.Resources.ERROR_TITLE, Properties.Resources.ERROR_403_MESSAGE));
            handleServerResponse.Add(System.Net.HttpStatusCode.BadRequest, async () =>
            {
                //Show create account dialog
                MessageDialogResult result = await this.ShowMessageAsync("", Properties.Resources.NO_EMAIL_CREATE,
                    MessageDialogStyle.AffirmativeAndNegative);

                if (result == MessageDialogResult.Affirmative)
                    HandleSuccessfulLogin(await controller.HandleCreateAccountAsync(token));
            });
            handleServerResponse.Add(System.Net.HttpStatusCode.InternalServerError, async () =>
            {
                await this.ShowMessageAsync(Properties.Resources.ERROR_TITLE, Properties.Resources.ERROR_500_MESSAGE);
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

     
            SettingsWindow settings = new SettingsWindow(sub, GUID);
            Application.Current.MainWindow = settings;
            this.Close();
            settings.Show();

        }

        private async void NextButton_Click(object sender, RoutedEventArgs e)
        {
            lockControllers(true);

            try
            {
                var credential = await GoogleWebAuthorizationBroker.AuthorizeAsync( 
                                        cs, new[] { "email"},
                                        "user",
                                        CancellationToken.None);

                await credential.RefreshTokenAsync(CancellationToken.None);
                String idToken = credential.Token.IdToken;
                token = idToken;

                HandleSuccessfulLogin(await controller.HandleLoginAsync(idToken));
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
            NextButton.IsEnabled = !showProgressCircle;
            ProgressCircle.IsActive = showProgressCircle;
        }
    }
}