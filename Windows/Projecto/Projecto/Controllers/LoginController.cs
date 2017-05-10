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

    }
}
