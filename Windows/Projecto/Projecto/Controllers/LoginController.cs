using Projecto.Service;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Projecto.Controllers
{
    public class LoginController
    {
        private readonly ProjectoAPI mAPI;

        public LoginController()
        {
            this.mAPI = new ProjectoAPI();
        }


        public async Task HandleLoginAsync(String username, String password)
        {
            /*
            var response = await mAPI.Authenticate(username, password);
            
            if(response.StatusCode != 200){
                throw new Exception(response.StatusCode);
            }

            response = await result.Content.ReadAsStringAsync();

            long token;
            long.TryParse(response, out token);

            return token;
            */

        }
        
    }
}
