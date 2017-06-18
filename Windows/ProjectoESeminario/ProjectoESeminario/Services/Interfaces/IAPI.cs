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
        Task<HttpResponseMessage> Push(long account, String data);

        /// <summary>
        /// Used when we need to push MIME(Multimedia: images, docs) to the server
        /// </summary>
        /// <param name="account"></param>
        /// <param name="data"></param>
        /// <returns></returns>
        Task<HttpResponseMessage> Push(long account, StreamContent data, byte[] image);

        /// <summary>
        /// Pull data of the user @account from the server 
        /// </summary>
        /// <param name="account"></param>
        /// <returns></returns>
        Task<HttpResponseMessage> Pull(long account);

        /// <summary>
        /// Try to authenticate user @username with password @password
        /// </summary>
        /// <param name="username"></param>
        /// <param name="password"></param>
        /// <returns></returns>
        Task<HttpResponseMessage> Authenticate(String username, String password);

        /// <summary>
        /// Creates an account with the username @username and password @password
        /// </summary>
        /// <param name="username"></param>
        /// <param name="password"></param>
        /// <returns></returns>
        Task<HttpResponseMessage> CreateAccount(String username, String password);

    }
}
