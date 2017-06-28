using Projecto.UI;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Threading.Tasks;

namespace Projecto.Service
{

    public class ProjectoAPI : IAPI
    {
        /// <summary>
        /// Server location
        /// </summary>
        private readonly static String mainServer = ConfigurationManager.AppSettings["serverURL"];

        /// <summary>
        /// URI for the textual push
        /// </summary>
        private readonly static String push = "push";

        /// <summary>
        /// URI for the MIME push
        /// </summary>
        private readonly static String pushMIME = "pushMIME";

        /// <summary>
        /// URI for pulling data
        /// </summary>
        private readonly static String pull = "pull";

        /// <summary>
        /// URI for registerDevice
        /// </summary>
        private readonly static String registerDevice = "registerDevice";

        /// <summary>
        /// URI used to create/authenticate user
        /// </summary>
        private readonly static String accountManagement = "account";


        public readonly static String socketURL = ConfigurationManager.AppSettings["socketURL"];

        /// <summary>
        /// Object to do the HTTP requests
        /// </summary>
        private readonly HttpClient httpClient;

        public ProjectoAPI() {
            this.httpClient = new HttpClient();
        }

        public async Task<HttpResponseMessage> Authenticate(string username, string password){
            try
            {
                return await httpClient.GetAsync(mainServer + accountManagement + "?account=" + username + "&password=" + password);
            }
            catch (Exception)
            {
                throw new WebExceptions(System.Net.HttpStatusCode.InternalServerError);
            }
        }

        public async Task<HttpResponseMessage> CreateAccount(string username, string password)
        {
            try { 
                var parameters = new Dictionary<string, string>();
                parameters["account"] = username;
                parameters["password"] = password;
                return await httpClient.PutAsync(mainServer + accountManagement, new FormUrlEncodedContent(parameters));
            }
            catch (Exception)
            {
                throw new WebExceptions(System.Net.HttpStatusCode.InternalServerError);
            }
        }

        public async Task<HttpResponseMessage> Pull(long account, string deviceID)
        {
            if (account == 0)
                return null;
            try { 
                return await httpClient.GetAsync(mainServer + pull + "?account=" + account);
            }
            catch (Exception)
            {
                throw new WebExceptions(System.Net.HttpStatusCode.InternalServerError);
            }
        }

        public async Task<HttpResponseMessage> Push(long account, string data, string deviceID)
        {
            if (account == 0)
                return null;
            try
            {
                var parameters = new Dictionary<string, string>();
                parameters["token"] = account + "";
                parameters["data"] = data;
                return await httpClient.PutAsync(mainServer + push, new FormUrlEncodedContent(parameters));
            }
            catch (Exception)
            {
                throw new WebExceptions(System.Net.HttpStatusCode.InternalServerError);
            }
        }

        public async Task<HttpResponseMessage> Push(long account, byte[] data, string filename, string filetype, string deviceID)
        {
            if (account == 0)
                return null;

            try
            {
                MultipartFormDataContent form = new MultipartFormDataContent();

                form.Add(new StringContent(account + ""), "token");

                var file = new ByteArrayContent(data);

                //For now we will only support images
                file.Headers.ContentType = MediaTypeHeaderValue.Parse("image/" + filetype);

                form.Add(file, "file", filename);

                return await httpClient.PostAsync(mainServer + pushMIME, form);
                
            }
            catch (Exception e)
            {
                throw new WebExceptions(System.Net.HttpStatusCode.InternalServerError);
            }
        }

        Task<HttpResponseMessage> IAPI.registerDevice(long account, string deviceID, bool deviceType)
        {
            throw new NotImplementedException();
        }
    }
}
