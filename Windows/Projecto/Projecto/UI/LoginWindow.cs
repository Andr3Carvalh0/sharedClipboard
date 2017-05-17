using Projecto.Controllers;
using System;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Forms;

namespace Projecto.UI
{
    public partial class LoginWindow : MetroFramework.Forms.MetroForm
    {
        LoginController controller;
        Dictionary<Type, Action<Exception>> actionsOnException = new Dictionary<Type, Action<Exception>>();
        Dictionary<System.Net.HttpStatusCode, Action> handleServerResponse = new Dictionary<System.Net.HttpStatusCode, Action>();

        public LoginWindow()
        {
            InitializeComponent();
            controller = new LoginController();
            LoadingPanel.Visible = false;

            //Setup
            actionsOnException.Add(typeof(UserException), (ex) => System.Windows.MessageBox.Show(((UserException)ex).simplerMessage));
            actionsOnException.Add(typeof(WebException), (ex) => {
                Action action = () => { };
                handleServerResponse.TryGetValue(((WebException)ex).simplerError, out action);

                action.Invoke();
            });

            handleServerResponse.Add(System.Net.HttpStatusCode.Forbidden, () => System.Windows.MessageBox.Show("The password isn't valid."));
            handleServerResponse.Add(System.Net.HttpStatusCode.BadRequest, async () =>
            {
                //Show create account dialog
                MessageBoxResult dialogResult = System.Windows.MessageBox.Show("It looks like an account with this email, doesn't exist.\nWould you like to create one", "Oppsy dopsy do...", MessageBoxButton.OKCancel);
                if (dialogResult == MessageBoxResult.OK)
                {
                    long userToken = await controller.HandleCreateAccountAsync(EmailField.Text, PasswordField.Text);
                    HandleSuccessfulLogin(userToken);
                }
            });
        }

        private void HandleSuccessfulLogin(long userToken)
        {
            Properties.Settings.Default.userToken = userToken;
            Properties.Settings.Default.Save();
            EntryPoint.switchToSettings();
        }

        private async void ProceedButton_Click(object sender, EventArgs e)
        {
            ProceedButton.Enabled = false;
            String email = EmailField.Text;
            String password = PasswordField.Text;


            try {
                long userToken = await controller.HandleLoginAsync(email, password);
                HandleSuccessfulLogin(userToken);

            } catch (Exception ex) {
                Action<Exception> actionToPerform = (Exception) => { };

                actionsOnException.TryGetValue(ex.GetType(), out actionToPerform);

                actionToPerform.Invoke(ex);
            }
            finally
            {
                ProceedButton.Enabled = true;
            }
        }

        private void EmailField_TextChanged(object sender, EventArgs e)
        {
            ProceedButton.Enabled = HandleProceedButtonVisibility();
        }

        private void PasswordField_TextChanged(object sender, EventArgs e)
        {
            ProceedButton.Enabled = HandleProceedButtonVisibility();
        }

        private bool HandleProceedButtonVisibility()
        {
            return EmailField.Text.Length > 0 && PasswordField.Text.Length > 0;
        }
    }
}
