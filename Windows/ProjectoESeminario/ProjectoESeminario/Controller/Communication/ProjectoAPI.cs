using Newtonsoft.Json;
using ProjectoESeminario.Controller.Communication.Interfaces;
using ProjectoESeminario.Exceptions;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Net.Http;
using System.Threading.Tasks;

namespace ProjectoESeminario.Controller.Communication
{

    public class ProjectoAPI : IAPI
    {
        /// <summary>
        /// Server location
        /// </summary>
        private readonly static String mainServer = ConfigurationManager.AppSettings["serverURL"];

        /// <summary>
        /// URI for registerDevice
        /// </summary>
        private readonly static String registerDevice_URL = "registerDevice";

        /// <summary>
        /// URI used to create/authenticate user
        /// </summary>
        private readonly static String accountManagement = "account";

        /// <summary>
        /// URI to get the websocket URL
        /// </summary>
        private readonly static String getSocket_URL = "socket";


        /// <summary>
        /// Object to do the HTTP requests
        /// </summary>
        private readonly HttpClient httpClient;

        public ProjectoAPI() {
            this.httpClient = new HttpClient();
        }

        //A template method that every request method follows.
        private async Task<HttpResponseMessage> TemplateMethod(String authorization, Func<Task<HttpResponseMessage>> action)
        {
            try
            {
                httpClient.DefaultRequestHeaders.Add("Authorization", authorization);

                return await action.Invoke();
            }
            catch (Exception)
            {
                throw new WebExceptions(System.Net.HttpStatusCode.InternalServerError);
            }
            finally
            {
                httpClient.DefaultRequestHeaders.Remove("Authorization");
            }
        }

        public async Task<HttpResponseMessage> Authenticate(String token) { return await TemplateMethod(token, async () => await httpClient.PostAsync(mainServer + accountManagement, null)); }

        public async Task<HttpResponseMessage> CreateAccount(String token) { return await TemplateMethod(token, async () => await httpClient.PutAsync(mainServer + accountManagement, null)); }

        public async Task<HttpResponseMessage> registerDevice(String sub, string deviceID, bool deviceType, String deviceName) {
            return await TemplateMethod(sub, async () => {

                var parameters = new Dictionary<string, string>
                {
                    ["deviceIdentifier"] = deviceID,
                    ["useSockets"] = true + "",
                    ["deviceName"] = deviceName
                };

                return await httpClient.PutAsync(mainServer + registerDevice_URL, new FormUrlEncodedContent(parameters));
            });
        }

        public async Task<HttpResponseMessage> getSocketURL()
        {
            return await httpClient.GetAsync(mainServer + getSocket_URL);      
        }
    }
}
