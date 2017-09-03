using ProjectoESeminario.Controller.Communication.Utils;
using ProjectoESeminario.Controller.Data.Cache;
using ProjectoESeminario.Controller.Data.Cache.Interface;
using ProjectoESeminario.Controller.State;
using ProjectoESeminario.Services.Interfaces;
using ProjectoESeminario.View.Settings;
using System;
using System.Collections.Generic;
using System.Drawing.Imaging;
using System.IO;
using System.Threading;
using System.Drawing;

namespace ProjectoESeminario.Services
{
    public class ParentListener : IParent, IParentListener
    {
        private readonly String TAG = "Portugal: ParentListener";
        private static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);
        private Dictionary<string, ImageFormat> supportedFormats = new Dictionary<string, ImageFormat>();
        private readonly IClipboardListener clipboardListener;
        private readonly IClipboardWebSocketListener clipboardWebSocketListener;
        private readonly ClipboardController clipboardController;
        private readonly ICache cache;
        private readonly String user;
        private readonly ISettingsController application;

        public ParentListener(String socketURL, String userID, String deviceID, ISettingsController settingsController)
        {
            this.clipboardListener = new ClipboardListener(this);
            this.clipboardWebSocketListener = new ClipboardWebSocketListener(socketURL, userID, deviceID, this);
            this.clipboardController = ClipboardControllerFactory.getSingleton("Welcome!");
            this.cache = new Cache();
            this.user = userID;
            this.supportedFormats.Add("jpg", ImageFormat.Jpeg);
            this.application = settingsController;
        }

        /// <summary>
    /// Method called when we receive text from the server
    /// </summary>
    /// <param name="text">text</param>
        public void OnReceive(string text)
        {
            OnCopy((s) =>
            {
                clipboardListener.UpdateClipboard(text);
                cache.Store(text);
            },
                text
            );
        }

        /// <summary>
        /// Called when we receive a image in the socket
        /// </summary>
        /// <param name="file"></param>
        public void OnReceive(ImageEx file)
        {
            OnCopy((s) =>
            {
                clipboardListener.UpdateClipboard(file.file);
                cache.Store(file);
            },
            file.path);
        }

        /// <summary>
        /// Method called when the user copies text on the pc
        /// </summary>
        /// <param name="text">The copied text</param>
        public void OnCopy(string text)
        {
            OnCopy((s) =>
            {
                clipboardWebSocketListener.HandleUpload(user, text);
                cache.Store(text);
            },
                text
            );
        }

        /// <summary>
        /// Called when the user copies a file.
        /// This method first verifies if the file is valid for us, and if so upload it to the server
        /// </summary>
        /// <param name="path">the file path</param>
        public void OnCopyMime(string path)
        {
            string[] tmp = path.Split('\\');
            string[] file_format = tmp[tmp.Length - 1].Split('.');

            ImageFormat format = null;

            if (supportedFormats.TryGetValue(file_format[file_format.Length - 1], out format))
            {
                log.Debug(TAG + " Upload file to server");

                byte[] image = File.ReadAllBytes(path);

                OnCopy((s) =>
                {
                    clipboardWebSocketListener.HandleUploadMime(user, image, tmp[tmp.Length - 1]);
                    cache.Store(ImageDecoder.decode(image, tmp[tmp.Length - 1]));
                },
                    path
                );
            }

        }

        /// <summary>
        /// Common method to all of the copy/receive methods
        /// </summary>
        /// <param name="run"></param>
        /// <param name="text"></param>
        private void OnCopy(Action<string> run, string text)
        {
            new Thread(() =>
            {
                try
                {
                    if (clipboardController.putValue(text))
                        run.Invoke(text);
                }
                finally
                {
                    clipboardController.wake();
                }
            }).Start();
        }

        /// <summary>
        /// Enables the service
        /// </summary>
        public void EnableService()
        {
            clipboardListener.Start();
            clipboardWebSocketListener.Start();
        }

        /// <summary>
        /// Disables the service
        /// </summary>
        public void DisableService()
        {
            clipboardListener.Stop();
            clipboardWebSocketListener.Stop();
        }

        /// <summary>
        /// Logs the user out of the application
        /// </summary>
        public void Logout()
        {
            cache.FlushAll();
            application.Logout();
        }

        /// <summary>
        /// Stops the app
        /// </summary>
        /// <param name="message">Message to show the user</param>
        public void StopApplication(string message)
        {
            application.StopApplication(message);
        }

        public void UpdateClipboard(Image image)
        {
            throw new NotImplementedException();
        }

        public void UpdateClipboard(string text)
        {
            //throw new NotImplementedException();
        }

        /// <summary>
        /// Gets every element of one category
        /// </summary>
        /// <param name="category"></param>
        /// <returns></returns>
        public string[] Pull(string category)
        {
            return cache.Pull(category);
        }
    }
}
