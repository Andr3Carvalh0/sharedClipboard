using Projecto.Service;
using Projecto.UI;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace Projecto.Controllers
{
    public class LoginController
    {
        private readonly ProjectoAPI mAPI;

        public LoginController()
        {
            this.mAPI = new ProjectoAPI();
        }

        public async Task<long> HandleCreateAccountAsync(String username, String password) {
            var response = await mAPI.CreateAccount(username, password);

            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new WebException(response.StatusCode);

            var responseBody = await response.Content.ReadAsStringAsync();

            long token;
            long.TryParse(responseBody, out token);

            return token;
        }

        public async Task<long> HandleLoginAsync(String username, String password)
        {
            bool isEmailValid = IsValidEmail(username);
            bool isPasswordValid = IsValidPassword(username);

            if(!isEmailValid || !isPasswordValid)
                throw new UserException(isEmailValid, isPasswordValid);

            var response = await mAPI.Authenticate(username, password);

            if (response.StatusCode != System.Net.HttpStatusCode.OK)
                throw new WebException(response.StatusCode);

            var responseBody = await response.Content.ReadAsStringAsync();

            long token;
            long.TryParse(responseBody, out token);

            return token;
        }

        private bool IsValidEmail(string username)
        {
            var configuration = @"^(([^<>()\[\]\\.,;:\s@']+(\.[^<>()\[\]\\.,;:\s@']+)*)|('.+'))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$";
            Regex r = new Regex(configuration);

            return r.IsMatch(username);
        }

        private bool IsValidPassword(string password)
        {
            return password.Length > 6;
        }
    }
}
