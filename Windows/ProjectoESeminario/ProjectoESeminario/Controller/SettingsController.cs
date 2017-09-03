using ProjectoESeminario.Controller.Communication;
using ProjectoESeminario.Exceptions;
using System;
using System.Threading.Tasks;

namespace ProjectoESeminario.Controller
{

    /// <summary>
    /// Responsable for the settings(If the service is enabled, if we should upload MIME Files)
    /// </summary>
    class SettingsController
    {
        private readonly String TAG = "Portugal: SettingsController";
        private static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);
        private readonly ProjectoAPI mAPI;
        
        public SettingsController()
        {
            log.Debug(TAG + " - Ctor");
            this.mAPI = new ProjectoAPI();
        }


        public async Task<String> GetSocketURL()
        {
            log.Debug(TAG + " method GetSocketURL called!");

            var response = await mAPI.getSocketURL();

            if (response.StatusCode != System.Net.HttpStatusCode.OK)
            {
                log.Error(TAG + " - WebException, statusCode:" + response.StatusCode);
                throw new WebExceptions(response.StatusCode);
            }

            return await response.Content.ReadAsStringAsync();
        }

    }
}
