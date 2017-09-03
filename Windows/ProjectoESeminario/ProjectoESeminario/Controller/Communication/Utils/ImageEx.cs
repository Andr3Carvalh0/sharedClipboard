using System;
using System.Drawing;

namespace ProjectoESeminario.Controller.Communication.Utils
{
    public class ImageEx
    {
        public String path { get; }
        public Image file { get; }

        public ImageEx(String path, Image file)
        {
            this.path = path;
            this.file = file;
        }

    }
}
