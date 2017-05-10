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

        public async void handleLoginAsync(String username, String password)
        {
            var response = await mAPI.Authenticate(username, password);
            int token;
            bool isNumeric = int.TryParse(response, out token);

            if (isNumeric)
            {
                Properties.Settings.Default.userToken = token;
                return;

            }

        }
    }
}
