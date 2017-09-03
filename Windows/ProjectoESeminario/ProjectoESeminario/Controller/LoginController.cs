using ProjectoESeminario.Controller.Communication;
using ProjectoESeminario.Exceptions;
using System;
using System.Threading.Tasks;

namespace ProjectoESeminario.Controller
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

        public async Task<String> HandleCreateAccountAsync(String token)
        {
            log.Debug(TAG + " method HandleCreateAccount called!");

            log.Debug(TAG + " Attempting to create an account");
            var response = await mAPI.CreateAccount(token);

            if (response.StatusCode != System.Net.HttpStatusCode.OK) {
                log.Error(TAG + " - WebException, statusCode:" + response.StatusCode);
                throw new WebExceptions(response.StatusCode);

            }

            var responseBody = await response.Content.ReadAsStringAsync();
            
            return responseBody;
        }

        public async void registerDevice(String sub, String GUID)
        {
            var response = await mAPI.registerDevice(sub, GUID, true, Environment.MachineName);

            if (response.StatusCode != System.Net.HttpStatusCode.OK)
            {
                log.Error(TAG + " - WebException, statusCode:" + response.StatusCode);
                throw new WebExceptions(response.StatusCode);
            }
            
        }

        public async Task<String> HandleLoginAsync(String token)
        {
            log.Debug(TAG + " method HandleLoginAsync called!");

            var response = await mAPI.Authenticate(token);

            if (response.StatusCode != System.Net.HttpStatusCode.OK)
            {
                log.Error(TAG + " - WebException, statusCode:" + response.StatusCode);
                throw new WebExceptions(response.StatusCode);
            }

           return await response.Content.ReadAsStringAsync();
        }
    }
}
