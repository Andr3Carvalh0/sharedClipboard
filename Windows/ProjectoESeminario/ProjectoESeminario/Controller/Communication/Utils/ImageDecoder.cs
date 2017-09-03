using System;
using System.Drawing;
using System.IO;

namespace ProjectoESeminario.Controller.Communication.Utils
{
    public static class ImageDecoder
    {
        public static ImageEx decode(String image, String path)
        {
            byte[] image_arr = Convert.FromBase64String(image);
            return ImageDecoder.decode(image_arr, path);

        }

        public static ImageEx decode(byte[] image, String path)
        {
            MemoryStream ms = new MemoryStream(image);
            Image image_final = Image.FromStream(ms);

            return new ImageEx(path, image_final);
        }


    }
}
