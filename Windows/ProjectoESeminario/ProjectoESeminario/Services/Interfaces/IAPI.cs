using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;


namespace Projecto.Service
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
