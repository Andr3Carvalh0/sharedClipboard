using ProjectoESeminario.Properties;
using ProjectoESeminario.View.History.Interfaces;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Imaging;
using System.IO;
using System.Resources;
using System.Windows.Controls;
using System.Windows.Media.Imaging;

namespace ProjectoESeminario.View.History.Category
{
    /// <summary>
    /// Interaction logic for History_Item.xaml
    /// </summary>
    public abstract partial class History_Item : UserControl
    {
        protected IHistory history;
        protected string name;

        public History_Item(string name, IHistory history)
        {
            InitializeComponent();
            Title.Content = name;
            this.name = name;
            this.history = history;
            Type.Source = transform((Bitmap)Properties.Resources.ResourceManager.GetObject(name.ToLower()));
            
        }
        private BitmapImage transform(Bitmap image)
        {

            using (var memory = new MemoryStream())
            {
                image.Save(memory, ImageFormat.Png);
                memory.Position = 0;

                var bitmapImage = new BitmapImage();
                bitmapImage.BeginInit();
                bitmapImage.StreamSource = memory;
                bitmapImage.CacheOption = BitmapCacheOption.OnLoad;
                bitmapImage.EndInit();

                return bitmapImage;
            }
        }

        public abstract void handleClick(object sender, System.Windows.Input.MouseButtonEventArgs e);
    }
}
