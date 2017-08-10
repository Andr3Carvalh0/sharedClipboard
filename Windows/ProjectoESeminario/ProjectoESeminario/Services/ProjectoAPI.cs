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

        public async Task<HttpResponseMessage> Authenticate(String token)
        {
            try
            {
                httpClient.DefaultRequestHeaders.Add("Authorization", token);

                return await httpClient.PostAsync(mainServer + accountManagement, null);
            }
            catch (Exception)
            {
                throw new WebExceptions(System.Net.HttpStatusCode.InternalServerError);
            }
        }

        public async Task<HttpResponseMessage> CreateAccount(String token)
        {
            try {
                httpClient.DefaultRequestHeaders.Add("Authorization", token);
                return await httpClient.PutAsync(mainServer + accountManagement, null);
            }
            catch (Exception)
            {
                throw new WebExceptions(System.Net.HttpStatusCode.InternalServerError);
            }
        }

        public async Task<HttpResponseMessage> Push(String sub, String data)
        {
            try
            {
                httpClient.DefaultRequestHeaders.Add("Authorization", sub);

                var parameters = new Dictionary<string, string>();
                parameters["data"] = data;
                return await httpClient.PutAsync(mainServer + push, new FormUrlEncodedContent(parameters));
            }
            catch (Exception)
            {
                throw new WebExceptions(System.Net.HttpStatusCode.InternalServerError);
            }
        }

        public async Task<HttpResponseMessage> Push(String sub, byte[] data, string filename, string filetype)
        {
            try
            {
                httpClient.DefaultRequestHeaders.Add("Authorization", sub);

                MultipartFormDataContent form = new MultipartFormDataContent();

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

        public async Task<HttpResponseMessage> registerDevice(String sub, string deviceID, bool deviceType, String deviceName)
        {
            try
            {
                httpClient.DefaultRequestHeaders.Add("Authorization", sub);

                var parameters = new Dictionary<string, string>();
                parameters["deviceIdentifier"] = deviceID;
                parameters["deviceType"] = false + "";
                parameters["deviceName"] = deviceName;

                return await httpClient.PutAsync(mainServer + registerDevice_URL, new FormUrlEncodedContent(parameters));

            }
            catch (Exception e)
            {
                throw new WebExceptions(System.Net.HttpStatusCode.InternalServerError);
            }
        }
    }
}
