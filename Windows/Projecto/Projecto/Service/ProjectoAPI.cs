using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
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

        public async Task<String> Authenticate(string username, string password)
        {
            if (!IsValidEmail(username) || !IsValidPassword(password))
                return null;

            var result = await httpClient.GetAsync(mainServer + accountManagement + "?account=" + username + "&password=" + password);

            return await result.Content.ReadAsStringAsync();
        }

        public async Task<String> CreateAccount(string username, string password)
        {
            if (!IsValidEmail(username) || !IsValidPassword(password))
                return null;

            var parameters = new Dictionary<string, string>();
            parameters["account"] = username;
            parameters["password"] = password;

            var result = await httpClient.PutAsync(mainServer + accountManagement, new FormUrlEncodedContent(parameters));

            return await result.Content.ReadAsStringAsync();
        }

        public async Task<HttpResponseMessage> Pull(long account)
        {
            if (account == 0)
                return null;

            return await httpClient.GetAsync(mainServer + accountManagement + "?account=" + account);
        }

        public void Push(long account, string data)
        {
            throw new NotImplementedException();
        }



        private bool IsValidEmail(string username)
        {
            return true;
        }

        private bool IsValidPassword(string password)
        {
            return true;
        }

    }
}
