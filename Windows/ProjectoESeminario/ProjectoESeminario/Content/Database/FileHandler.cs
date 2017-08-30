using ProjectoESeminario.DTOs;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;

namespace ProjectoESeminario.Databases
{
    public class FileHandler : ParentHandler
    {

        public void store(ImageEx image)
        {
            if (!projectoFolderExists())
                createProjectoFolder();

            String parent = Path.Combine(getPath(), "images");
            String file = Path.Combine(parent, image.path);

            image.file.Save(file);
        }

        public LinkedList<String> fetch()
        {
            try
            {
                return new LinkedList<String>(Directory.EnumerateFiles(Path.Combine(getPath(), "images")));
            }
            catch (Exception)
            {
                return new LinkedList<string>();
            }
        }


    }
}
