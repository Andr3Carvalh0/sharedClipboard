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
        private readonly static String registerDevice_URL = "registerDevice";

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

        public async Task<HttpResponseMessage> Push(String sub, String data) {
            return await TemplateMethod(sub, async () => {
                var parameters = new Dictionary<string, string>();
                parameters["data"] = data;
                return await httpClient.PutAsync(mainServer + push, new FormUrlEncodedContent(parameters));
            });
        }

        public async Task<HttpResponseMessage> Push(String sub, byte[] data, string filename, string filetype) {
            return await TemplateMethod(sub, async () => {

                MultipartFormDataContent form = new MultipartFormDataContent();
                var file = new ByteArrayContent(data);
                file.Headers.ContentType = MediaTypeHeaderValue.Parse("image/" + filetype);
                form.Add(file, "file", filename);

                return await httpClient.PostAsync(mainServer + pushMIME, form);
            });
        }

        public async Task<HttpResponseMessage> registerDevice(String sub, string deviceID, bool deviceType, String deviceName) {
            return await TemplateMethod(sub, async () => {

                var parameters = new Dictionary<string, string>();
                parameters["deviceIdentifier"] = deviceID;
                parameters["isMobile"] = false + "";
                parameters["deviceName"] = deviceName;

                return await httpClient.PutAsync(mainServer + registerDevice_URL, new FormUrlEncodedContent(parameters));
            });
        }
    }
}
