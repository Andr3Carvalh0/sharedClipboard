using System;
using System.Net.Http;
using System.Threading.Tasks;


namespace ProjectoESeminario.Controller.Communication.Interfaces
{
    /// <summary>
    /// Declaration of every operation we can do.
    /// </summary>
    public interface IAPI
    {
        Task<HttpResponseMessage> Authenticate(String token);

        Task<HttpResponseMessage> CreateAccount(String token);

        Task<HttpResponseMessage> registerDevice(String sub, string deviceID, bool deviceType, String deviceName);

        Task<HttpResponseMessage> getSocketURL();

    }
}
