using System;
using System.IO;

namespace ProjectoESeminario.Databases
{
    public abstract class ParentHandler
    {
        protected readonly String folder_parent = Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData);
        protected readonly String folder_name = "Projecto";

        protected bool projectoFolderExists()
        {
            String path = Path.Combine(folder_parent, folder_name);
            return Directory.Exists(path);
        }

        protected void createProjectoFolder()
        {
            String path = Path.Combine(folder_parent, folder_name);
            Directory.CreateDirectory(path);
        }

        protected String getPath()
        {
            return Path.Combine(folder_parent, folder_name);
        }

        public void destroyAll()
        {
            DirectoryInfo di = new DirectoryInfo(getPath());

            foreach (FileInfo file in di.GetFiles())
            {
                file.Delete();
            }

            foreach (DirectoryInfo dir in di.GetDirectories())
            {
                dir.Delete(true);
            }
        }
    }
}
