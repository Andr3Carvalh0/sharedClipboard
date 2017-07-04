using Projecto.Service;
using Projecto.UI;
using System;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace Projecto.Controllers
{
    /// <summary>
    /// Responsable for the authentication/creation of an account
    /// </summary>
    public class LoginController
    {
        private readonly String TAG = "Portugal: LoginController";
        private readonly ProjectoAPI mAPI;
        private static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);

        public LoginController() {
            log.Debug(TAG + " - Ctor");
            this.mAPI = new ProjectoAPI();
        }

        public async Task<long> HandleCreateAccountAsync(String username, String password)
        {
            log.Debug(TAG + " method HandleCreateAccount called!");

            log.Debug(TAG + " Attempting to create an account");
            var response = await mAPI.CreateAccount(username, password);

            if (response.StatusCode != System.Net.HttpStatusCode.OK) {
                log.Error(TAG + " - WebException, statusCode:" + response.StatusCode);
                throw new WebExceptions(response.StatusCode);

            }

            var responseBody = await response.Content.ReadAsStringAsync();

            long token;
            long.TryParse(responseBody, out token);

            return token;
        }

        public async void registerDevice(long userToken, String GUID)
        {
            var response = await mAPI.registerDevice(userToken, GUID, true, Environment.MachineName);

            if (response.StatusCode != System.Net.HttpStatusCode.OK)
            {
                log.Error(TAG + " - WebException, statusCode:" + response.StatusCode);
                throw new WebExceptions(response.StatusCode);
            }
            
        }

        public async Task<long> HandleLoginAsync(String username, String password)
        {
            log.Debug(TAG + " method HandleLoginAsync called!");

            bool isEmailValid = IsValidEmail(username);
            bool isPasswordValid = IsValidPassword(username);

            if (!isEmailValid || !isPasswordValid)
            {
                log.Error(TAG + " - UserException");
                throw new UserExceptions(isEmailValid, isPasswordValid);
            }
            var response = await mAPI.Authenticate(username, password);

            if (response.StatusCode != System.Net.HttpStatusCode.OK)
            {
                log.Error(TAG + " - WebException, statusCode:" + response.StatusCode);
                throw new WebExceptions(response.StatusCode);
            }

            var responseBody = await response.Content.ReadAsStringAsync();

            long token;
            long.TryParse(responseBody, out token);

            if (token == 0) {
                log.Error(TAG + " - Heroku exception(probably), the server is sleeping, and the response was 200, but it isnt valid, since we dont have content");
                throw new WebExceptions(System.Net.HttpStatusCode.InternalServerError);
            }
            return token;
        }

        /// <summary>
        /// Used to check if the inputted email is a valid email.
        /// The regex expression was taken out the chromium source code
        /// </summary>
        /// <param name="username"></param>
        /// <returns></returns>
        private bool IsValidEmail(string username)
        {
            String configuration = @"^(([^<>()\[\]\\.,;:\s@']+(\.[^<>()\[\]\\.,;:\s@']+)*)|('.+'))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$";
            Regex rgx = new Regex(configuration);

            return rgx.IsMatch(username);
        }

        /// <summary>
        /// Used to check if the password that user inputted is valid.
        /// We just validate if the password length is greater that 6.
        /// </summary>
        /// <param name="password"></param>
        /// <returns></returns>
        private bool IsValidPassword(string password)
        {
            return password.Length > 6;
        }
    }
}
