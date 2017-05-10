using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;


namespace Projecto.Service
{

    public interface IAPI
    {
        void Push(long account, String data);
        Task<HttpResponseMessage> Pull(long account);
        Task<String> Authenticate(String username, String password);
        Task<String> CreateAccount(String username, String password);

    }
}
