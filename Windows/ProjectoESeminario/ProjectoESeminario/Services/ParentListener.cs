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
using System.Runtime.InteropServices;
using System.Windows;

namespace ProjectoESeminario.Services
{
    public class ParentListener : IParent, IParentListener
    {
        private readonly String TAG = "Portugal: ParentListener";
        private static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);
        private readonly Dictionary<string, ImageFormat> supportedFormats = new Dictionary<string, ImageFormat>();
        private readonly IClipboardListener clipboardListener;
        private readonly IClipboardWebSocketListener clipboardWebSocketListener;
        private readonly ClipboardController clipboardController;
        private readonly ICache cache;
        private readonly String user;
        private readonly String device;
        private readonly ISettingsController application;

        public ParentListener(String socketURL, String userID, String deviceID, long order, ISettingsController settingsController)
        {
            this.clipboardListener = new ClipboardListener(this);
            this.clipboardWebSocketListener = new ClipboardWebSocketListener(socketURL, userID, deviceID, this);
            this.clipboardController = ClipboardControllerFactory.getSingleton(order);
            this.cache = new Cache();
            this.user = userID;
            this.device = deviceID;
            this.supportedFormats.Add("jpg", ImageFormat.Jpeg);
            this.application = settingsController;
        }

        /// <summary>
        /// Method called when we receive text from the server
        /// </summary>
        /// <param name="text">text</param>
        /// <param name="order">the order received</param>
        public void OnReceive(string text, int order)
        {
            OnCopy(
                (s) =>
                {
                    clipboardListener.UpdateClipboard(s);
                    cache.Store(s);
                },
                (s) =>
                {
                    cache.Store(s);
                },
                text,
                order
            );
        }

        /// <summary>
        /// Called when we receive a image in the socket
        /// </summary>
        /// <param name="file"></param>
        public void OnReceive(ImageEx file, int order)
        {
            OnCopy((s) =>
                {
                    clipboardListener.UpdateClipboard(file.path, file.file);
                    cache.Store(file);
                },
                (s) =>
                {
                    cache.Store(file);
                },
                file.path,
                order);
        }

        /// <summary>
        /// Method called when the user copies text on the pc
        /// </summary>
        /// <param name="text">The copied text</param>
        public void OnCopy(string text)
        {
            OnCopy(
                () => clipboardWebSocketListener.HandleUpload(user, text, device), 
                (s) => cache.Store(s), 
                text);
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


                OnCopy(
                    () => clipboardWebSocketListener.HandleUploadMime(user, image, tmp[tmp.Length - 1], device),
                    (s) => cache.Store(ImageDecoder.decode(image, tmp[tmp.Length - 1])),
                    path
                );
            }

        }

        public void OnUploadReport(long order)
        {
            clipboardController.UpdateStateOfUpload(order);
        }

        /// <summary>
        /// Common method to all of the copy methods of local trigger
        /// </summary>
        /// <param name="run"></param>
        /// <param name="text"></param>
        private void OnCopy(Action upload, Action<string> OnEnd, string text)
        {
            new Thread(() =>
            {
                try
                {
                    if
                    (
                        clipboardController
                            .PutValue(text, 
                                      (p) =>
                                            {
                                            new Thread(() =>
                                            {
                                                upload.Invoke();
                                                Thread.Sleep(5000);
                                                clipboardController.RemoveUpload(p);
                                            }).Start();
                                        })
                    )
                    {
                        OnEnd.Invoke(text);
                    }
                }
                finally
                {
                    clipboardController.Wake();
                }
            }).Start();
        }

        /// <summary>
        /// Common method to all of the receive methods
        /// </summary>
        /// <param name="run"></param>
        /// <param name="text"></param>
        /// <param name="order">the number of order received</param>
        private void OnCopy(Action<string> change, Action<string> save, string text, int order)
        {
            new Thread(() =>
            {
                int value = clipboardController.PutValue(text, order);

                if (value == 1)
                {
                    change.Invoke(text);
                    return;
                }

                if(value == 2)
                    save.Invoke(text);     

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

        public void UpdateClipboard(Image image, string path)
        {
            UpdateCommon(path, () =>
            {
                clipboardListener.UpdateClipboard(path, image);
                Notify(Properties.Resources.COPIED_IMAGE);
            });          
        }

        public void UpdateClipboard(string text)
        {
            UpdateCommon(text, () =>
            {
                clipboardListener.UpdateClipboard(text);
                Notify(Properties.Resources.COPIED_TEXT + text);
            });
        }

        private void UpdateCommon(string text, Action toRun)
        {
            clipboardController.AddToIgnoreQueue(text);

            if (!clipboardController.PutValue(text, (p) => { return; }))
            {
                toRun.Invoke();
            }
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

        /// <summary>
        /// Gets every saved image
        /// </summary>
        /// <returns></returns>
        public string[] Pull()
        {
            return cache.Pull();
        }

        private void Notify(string text)
        {
            MessageBox.Show(text);
        }

        public void SaveOrder()
        {
            Properties.Settings.Default.order = clipboardController.GetOrder();
        }
    }
}
