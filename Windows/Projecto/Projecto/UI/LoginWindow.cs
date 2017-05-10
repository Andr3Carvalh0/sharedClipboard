using Projecto.Controllers;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Projecto.UI
{
    public partial class LoginWindow : Form
    {
        LoginController controller;

        public LoginWindow()
        {
            InitializeComponent();
            controller = new LoginController();

        }

        private void ProceedButton_Click(object sender, EventArgs e)
        {
            String email = EmailField.Text;
            String password = PasswordField.Text;

            try{
                controller.handleLoginAsync(email, password);
            }catch(Exception e){
                //Show message here
            }
        }

    }
}
