using ProjectoESeminario.DTOs;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;

namespace ProjectoESeminario.Databases
{
    public class FileHandler : ParentHandler
    {

        public ImageEx store(byte[] image_arr, String filename)
        {
            if (!projectoFolderExists())
                createProjectoFolder();

            String file = Path.Combine(getPath(), filename);

            MemoryStream ms = new MemoryStream(image_arr);
            Image image = Image.FromStream(ms);

            image.Save(file);

            return new ImageEx(file, image);
        }




    }
}
