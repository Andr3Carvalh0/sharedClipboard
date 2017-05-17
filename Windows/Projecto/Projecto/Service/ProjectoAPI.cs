using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace Projecto.Service
{
    public class ProjectoAPI : IAPI
    {
        private readonly static String mainServer = "http://projecto-testes.herokuapp.com/api/";
        private readonly static String push = "push";
        private readonly static String pull = "pull";
        private readonly static String accountManagement = "account";

        private readonly HttpClient httpClient;

        public ProjectoAPI()
        {
            this.httpClient = new HttpClient();
        }

        public async Task<HttpResponseMessage> Authenticate(string username, string password)
        {
            if (!IsValidEmail(username) || !IsValidPassword(password))
                return null;

            return await httpClient.GetAsync(mainServer + accountManagement + "?account=" + username + "&password=" + password);
        }

        public async Task<HttpResponseMessage> CreateAccount(string username, string password)
        {
            if (!IsValidEmail(username) || !IsValidPassword(password))
                return null;

            var parameters = new Dictionary<string, string>();
            parameters["account"] = username;
            parameters["password"] = password;
            return await httpClient.PutAsync(mainServer + accountManagement, new FormUrlEncodedContent(parameters));
        }

        public async Task<HttpResponseMessage> Pull(long account)
        {
            if (account == 0)
                return null;

            return await httpClient.GetAsync(mainServer + pull + "?account=" + account);
        }

        public async Task<HttpResponseMessage> Push(long account, string data)
        {
            if (account == 0)
                return null;

            var parameters = new Dictionary<string, string>();
            parameters["token"] = account + "";
            parameters["data"] = data;
            return await httpClient.PutAsync(mainServer + push, new FormUrlEncodedContent(parameters));

        }

        private bool IsValidEmail(string username)
        {
            //let configuration = "/^(([^<>()\[\]\\.,;:\s@']+(\.[^<>()\[\]\\.,;:\s@']+)*)|('.+'))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/";
            // Regex r = new Regex(configuration);

            return false;
            //return r.IsMatch(username);
        }

        private bool IsValidPassword(string password)
        {
            return password.Length > 6;
        }

    }
}
