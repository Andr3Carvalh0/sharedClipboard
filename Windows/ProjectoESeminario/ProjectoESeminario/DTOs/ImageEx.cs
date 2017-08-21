using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ProjectoESeminario.DTOs
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
