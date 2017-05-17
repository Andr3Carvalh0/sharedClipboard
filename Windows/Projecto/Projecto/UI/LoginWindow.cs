using Projecto.Controllers;
using System;

namespace Projecto.UI
{
    public partial class LoginWindow : MetroFramework.Forms.MetroForm
    {
        LoginController controller;

        public LoginWindow()
        {
            InitializeComponent();
            controller = new LoginController();
            ProceedButton.Enabled = false;
        }

        private void ProceedButton_Click(object sender, EventArgs e)
        {
            String email = EmailField.Text;
            String password = PasswordField.Text;

            try{
                controller.HandleLoginAsync(email, password);
            }catch(Exception){
                //Show message here
            }
        }

        private void EmailField_TextChanged(object sender, EventArgs e)
        {
            ProceedButton.Enabled = HandleProceedButtonVisibility();
        }

        private void PasswordField_TextChanged(object sender, EventArgs e)
        {
            ProceedButton.Enabled = HandleProceedButtonVisibility();
        }

        private bool HandleProceedButtonVisibility()
        {
            return EmailField.TextLength > 0 && PasswordField.TextLength > 0;
        }
    }
}
