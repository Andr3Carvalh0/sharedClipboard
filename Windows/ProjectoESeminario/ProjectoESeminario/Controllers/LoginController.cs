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
        private readonly ProjectoAPI mAPI;

        public LoginController() {
            this.mAPI = new ProjectoAPI();
        }

        public async Task<long> HandleCreateAccountAsync(String username, String password)
        {
            var response = await mAPI.CreateAccount(username, password);

            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new WebExceptions(response.StatusCode);

            var responseBody = await response.Content.ReadAsStringAsync();

            long token;
            long.TryParse(responseBody, out token);

            return token;
        }

        public async Task<long> HandleLoginAsync(String username, String password)
        {
            bool isEmailValid = IsValidEmail(username);
            bool isPasswordValid = IsValidPassword(username);

            if (!isEmailValid || !isPasswordValid)
                throw new UserExceptions(isEmailValid, isPasswordValid);

            var response = await mAPI.Authenticate(username, password);

            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new WebExceptions(response.StatusCode);

            var responseBody = await response.Content.ReadAsStringAsync();

            long token;
            long.TryParse(responseBody, out token);

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
