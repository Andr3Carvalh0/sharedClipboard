using ProjectoESeminario.View.History.Interfaces;
using System;
using System.Drawing;
using System.IO;
using System.Threading;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media.Imaging;

namespace ProjectoESeminario.UI.History.Category_Detailed
{
    /// <summary>
    /// Interaction logic for History_Image_Item.xaml
    /// </summary>
    public partial class History_Image_Item : UserControl
    {
        private readonly IHistory history;
        private readonly string path;
        private volatile int hasClicked = 0;
        private readonly BitmapImage image;

        public History_Image_Item(String path, IHistory history)
        {
            InitializeComponent();
            this.history = history;
            this.path = path;
            this.image = new BitmapImage(new Uri(path, UriKind.Absolute));
            Image.MouseLeftButtonDown += PrepareClickEvent;
            Image.MouseLeftButtonUp += ClickEvent;

            Image.Source = image;
        }

        private void PrepareClickEvent(object sender, MouseButtonEventArgs e)
        {
            Interlocked.CompareExchange(ref hasClicked, 1, 0);

            new Thread(() =>
            {
                Thread.Sleep(1000);
                Interlocked.CompareExchange(ref hasClicked, 0, 1);

            }).Start();
        }

        private void ClickEvent(object sender, MouseButtonEventArgs e)
        {
            if (hasClicked > 0)
                history.SetContent(transform(image), path);
        }

        private Bitmap transform(BitmapImage image)
        {
            using (MemoryStream outStream = new MemoryStream())
            {
                BitmapEncoder enc = new BmpBitmapEncoder();
                enc.Frames.Add(BitmapFrame.Create(image));
                enc.Save(outStream);
                System.Drawing.Bitmap bitmap = new System.Drawing.Bitmap(outStream);

                return new Bitmap(bitmap);
            }
        }
    }
}
