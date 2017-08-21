﻿using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ProjectoESeminario.Databases
{
    public class FileHandler
    {
        private readonly String folder_parent = Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData);
        private readonly String folder_name = "Projecto";

        public void store(Image image, String filename)
        {
            if (!projectoFolderExists())
                createProjectoFolder();

            String path = Path.Combine(folder_parent, folder_name);
            String file = Path.Combine(path, filename);
            image.Save(file);
        }

        private bool projectoFolderExists() {
            String path = Path.Combine(folder_parent, folder_name);
            return Directory.Exists(path);
        }

        private void createProjectoFolder()
        {
            String path = Path.Combine(folder_parent, folder_name);
            Directory.CreateDirectory(path);
        }
    }
}
