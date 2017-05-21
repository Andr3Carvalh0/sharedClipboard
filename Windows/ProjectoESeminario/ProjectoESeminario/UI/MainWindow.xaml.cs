using MahApps.Metro.Controls;
using MahApps.Metro.Controls.Dialogs;
using Projecto.Controllers;
using Projecto.UI;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices;
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

namespace ProjectoESeminario
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : MetroWindow
    {

        
       
        LoginController controller;
        Dictionary<Type, Action<Exception>> actionsOnException = new Dictionary<Type, Action<Exception>>();
        Dictionary<System.Net.HttpStatusCode, Action> handleServerResponse = new Dictionary<System.Net.HttpStatusCode, Action>();

        public MainWindow()
        {
            InitializeComponent();

            ClipboardListener a = new ClipboardListener();

            controller = new LoginController();

            //Setup
            actionsOnException.Add(typeof(UserExceptions), (ex) => System.Windows.MessageBox.Show(((UserExceptions)ex).simplerMessage));
            actionsOnException.Add(typeof(WebExceptions), (ex) =>
            {
                Action action = () => { };
                handleServerResponse.TryGetValue(((WebExceptions)ex).simplerError, out action);

                action.Invoke();
            });

            handleServerResponse.Add(System.Net.HttpStatusCode.Forbidden, async () => await this.ShowMessageAsync("Ops...", "The password isn't valid."));
            handleServerResponse.Add(System.Net.HttpStatusCode.BadRequest, async () =>
            {
                //Show create account dialog
                MessageBoxResult dialogResult = System.Windows.MessageBox.Show("It looks like an account with this email, doesn't exist.\nWould you like to create one", "", MessageBoxButton.OKCancel);
                if (dialogResult == MessageBoxResult.OK)
                {
                    long userToken = await controller.HandleCreateAccountAsync(EmailField.Text, PasswordField.Password);
                    HandleSuccessfulLogin(userToken);
                }
            });
        }

        private void HandleSuccessfulLogin(long userToken)
        {
            Properties.Settings.Default.userToken = userToken;
            Properties.Settings.Default.Save();
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
            NextButton.IsEnabled = false;
            String email = EmailField.Text;
            String password = PasswordField.Password;

            try
            {
                long userToken = await controller.HandleLoginAsync(email, password);

                HandleSuccessfulLogin(userToken);

            }
            catch (Exception ex)
            {
                Action<Exception> actionToPerform = (Exception) => { };

                actionsOnException.TryGetValue(ex.GetType(), out actionToPerform);

                actionToPerform.Invoke(ex);
            }
            finally
            {
                NextButton.IsEnabled = true;
            }
        }
    }


    internal static class NativeMethods
    {


    }

}