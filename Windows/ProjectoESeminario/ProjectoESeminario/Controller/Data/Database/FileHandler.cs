using ProjectoESeminario.Controller.Communication.Utils;
using ProjectoESeminario.Controller.Data.Database.Interface;
using System;
using System.Collections.Generic;
using System.IO;

namespace ProjectoESeminario.Controller.Data.Database
{
    public class FileHandler : ParentHandler
    {

        public void Store(ImageEx image)
        {
            if (!ProjectoFolderExists())
                CreateProjectoFolder();

            String parent = Path.Combine(GetPath(), "images");
            Directory.CreateDirectory(parent);
            String file = Path.Combine(parent, image.path);

            image.file.Save(file);
        }

        public LinkedList<String> Fetch()
        {
            try
            {
                return new LinkedList<String>(Directory.EnumerateFiles(Path.Combine(GetPath(), "images")));
            }
            catch (Exception)
            {
                return new LinkedList<string>();
            }
        }


    }
}
