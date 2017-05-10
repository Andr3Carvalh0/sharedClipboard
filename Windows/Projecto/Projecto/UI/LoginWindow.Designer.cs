namespace Projecto.UI
{
    partial class LoginWindow
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.ProceedButton = new System.Windows.Forms.Button();
            this.EmailField = new System.Windows.Forms.TextBox();
            this.PasswordField = new System.Windows.Forms.TextBox();
            this.SuspendLayout();
            // 
            // ProceedButton
            // 
            this.ProceedButton.Location = new System.Drawing.Point(712, 367);
            this.ProceedButton.Name = "ProceedButton";
            this.ProceedButton.Size = new System.Drawing.Size(127, 46);
            this.ProceedButton.TabIndex = 0;
            this.ProceedButton.Text = "Next";
            this.ProceedButton.UseVisualStyleBackColor = true;
            this.ProceedButton.Click += new System.EventHandler(this.ProceedButton_Click);
            // 
            // EmailField
            // 
            this.EmailField.Location = new System.Drawing.Point(100, 152);
            this.EmailField.Name = "EmailField";
            this.EmailField.Size = new System.Drawing.Size(422, 31);
            this.EmailField.TabIndex = 1;
            // 
            // PasswordField
            // 
            this.PasswordField.Location = new System.Drawing.Point(100, 201);
            this.PasswordField.Name = "PasswordField";
            this.PasswordField.Size = new System.Drawing.Size(422, 31);
            this.PasswordField.TabIndex = 2;
            // 
            // LoginWindow
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(12F, 25F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(851, 425);
            this.Controls.Add(this.PasswordField);
            this.Controls.Add(this.EmailField);
            this.Controls.Add(this.ProceedButton);
            this.Name = "LoginWindow";
            this.Text = "LoginWindow";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button ProceedButton;
        private System.Windows.Forms.TextBox EmailField;
        private System.Windows.Forms.TextBox PasswordField;
    }
}