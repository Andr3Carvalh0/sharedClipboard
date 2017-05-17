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
            this.EmailField = new System.Windows.Forms.TextBox();
            this.Spinner = new MetroFramework.Controls.MetroProgressSpinner();
            this.PasswordField = new System.Windows.Forms.TextBox();
            this.label1 = new MetroFramework.Controls.MetroLabel();
            this.label2 = new MetroFramework.Controls.MetroLabel();
            this.label3 = new System.Windows.Forms.Label();
            this.backgroundWorker1 = new System.ComponentModel.BackgroundWorker();
            this.MainContent = new System.Windows.Forms.Panel();
            this.LoadingPanel = new System.Windows.Forms.Panel();
            this.ProceedButton = new System.Windows.Forms.Button();
            this.MainContent.SuspendLayout();
            this.LoadingPanel.SuspendLayout();
            this.SuspendLayout();
            // 
            // EmailField
            // 
            this.EmailField.Location = new System.Drawing.Point(24, 29);
            this.EmailField.Margin = new System.Windows.Forms.Padding(2);
            this.EmailField.Name = "EmailField";
            this.EmailField.Size = new System.Drawing.Size(391, 20);
            this.EmailField.TabIndex = 1;
            this.EmailField.TextChanged += new System.EventHandler(this.EmailField_TextChanged);
            // 
            // Spinner
            // 
            this.Spinner.Location = new System.Drawing.Point(19, 21);
            this.Spinner.Maximum = 100;
            this.Spinner.Name = "Spinner";
            this.Spinner.Size = new System.Drawing.Size(35, 36);
            this.Spinner.Speed = 2F;
            this.Spinner.Style = MetroFramework.MetroColorStyle.Blue;
            this.Spinner.TabIndex = 3;
            // 
            // PasswordField
            // 
            this.PasswordField.Location = new System.Drawing.Point(24, 82);
            this.PasswordField.Margin = new System.Windows.Forms.Padding(2);
            this.PasswordField.Name = "PasswordField";
            this.PasswordField.PasswordChar = '*';
            this.PasswordField.Size = new System.Drawing.Size(391, 20);
            this.PasswordField.TabIndex = 2;
            this.PasswordField.TextChanged += new System.EventHandler(this.PasswordField_TextChanged);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(21, 8);
            this.label1.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(41, 19);
            this.label1.TabIndex = 3;
            this.label1.Text = "Email";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(20, 61);
            this.label2.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(63, 19);
            this.label2.TabIndex = 4;
            this.label2.Text = "Password";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(80, 54);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(0, 13);
            this.label3.TabIndex = 6;
            // 
            // MainContent
            // 
            this.MainContent.Controls.Add(this.LoadingPanel);
            this.MainContent.Controls.Add(this.ProceedButton);
            this.MainContent.Controls.Add(this.EmailField);
            this.MainContent.Controls.Add(this.label3);
            this.MainContent.Controls.Add(this.PasswordField);
            this.MainContent.Controls.Add(this.label1);
            this.MainContent.Controls.Add(this.label2);
            this.MainContent.Location = new System.Drawing.Point(-1, 63);
            this.MainContent.Name = "MainContent";
            this.MainContent.Size = new System.Drawing.Size(442, 156);
            this.MainContent.TabIndex = 7;
            // 
            // LoadingPanel
            // 
            this.LoadingPanel.BackColor = System.Drawing.Color.White;
            this.LoadingPanel.Controls.Add(this.Spinner);
            this.LoadingPanel.Location = new System.Drawing.Point(2, 20);
            this.LoadingPanel.Name = "LoadingPanel";
            this.LoadingPanel.Size = new System.Drawing.Size(442, 71);
            this.LoadingPanel.TabIndex = 8;
            // 
            // ProceedButton
            // 
            this.ProceedButton.Location = new System.Drawing.Point(352, 121);
            this.ProceedButton.Name = "ProceedButton";
            this.ProceedButton.Size = new System.Drawing.Size(63, 23);
            this.ProceedButton.TabIndex = 7;
            this.ProceedButton.Text = "Next";
            this.ProceedButton.UseVisualStyleBackColor = true;
            this.ProceedButton.Click += new System.EventHandler(this.ProceedButton_Click);
            // 
            // LoginWindow
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(440, 220);
            this.Controls.Add(this.MainContent);
            this.Margin = new System.Windows.Forms.Padding(2);
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "LoginWindow";
            this.Padding = new System.Windows.Forms.Padding(10, 60, 10, 10);
            this.Resizable = false;
            this.Text = "Projecto e Seminario";
            this.MainContent.ResumeLayout(false);
            this.MainContent.PerformLayout();
            this.LoadingPanel.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion
        private System.Windows.Forms.TextBox EmailField;
        private System.Windows.Forms.TextBox PasswordField;
        private MetroFramework.Controls.MetroLabel label1;
        private MetroFramework.Controls.MetroLabel label2;
        private MetroFramework.Controls.MetroProgressSpinner Spinner;
        private System.Windows.Forms.Label label3;
        private System.ComponentModel.BackgroundWorker backgroundWorker1;
        private System.Windows.Forms.Panel MainContent;
        private System.Windows.Forms.Button ProceedButton;
        private System.Windows.Forms.Panel LoadingPanel;
    }
}