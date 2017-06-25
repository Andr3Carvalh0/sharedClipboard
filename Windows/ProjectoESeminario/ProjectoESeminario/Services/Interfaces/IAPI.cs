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
        /// <summary>
        /// Used when we need to push textual data to the server
        /// </summary>
        /// <param name="account"></param>
        /// <param name="data"></param>
        /// <returns></returns>
        Task<HttpResponseMessage> Push(long account, string data, string deviceID);

        /// <summary>
        /// Used when we need to push MIME(Multimedia: images, docs) to the server
        /// </summary>
        /// <param name="account"></param>
        /// <param name="data"></param>
        /// <returns></returns>
        Task<HttpResponseMessage> Push(long account, byte[] data, string filename, string filetype, string deviceID);

        /// <summary>
        /// Pull data of the user @account from the server 
        /// </summary>
        /// <param name="account"></param>
        /// <returns></returns>
        Task<HttpResponseMessage> Pull(long account, string deviceID);

        /// <summary>
        /// Try to authenticate user @username with password @password
        /// </summary>
        /// <param name="username"></param>
        /// <param name="password"></param>
        /// <returns></returns>
        Task<HttpResponseMessage> Authenticate(string username, string password);

        /// <summary>
        /// Creates an account with the username @username and password @password
        /// </summary>
        /// <param name="username"></param>
        /// <param name="password"></param>
        /// <returns></returns>
        Task<HttpResponseMessage> CreateAccount(string username, string password);

        /// <summary>
        /// Registers the device on this account so it can obtain information
        /// </summary>
        /// <param name="account"></param>
        /// <param name="deviceID"></param>
        /// <param name="deviceType"> false: Desktop, true mobile</param>
        /// <returns></returns>
        Task<HttpResponseMessage> registerDevice(long account, string deviceID, bool deviceType);


    }
}
