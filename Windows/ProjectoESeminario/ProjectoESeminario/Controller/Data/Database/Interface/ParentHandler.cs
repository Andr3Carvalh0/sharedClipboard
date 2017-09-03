using System;
using System.IO;

namespace ProjectoESeminario.Controller.Data.Database.Interface
{
    public abstract class ParentHandler
    {
        protected readonly String folder_parent = Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData);
        protected readonly String folder_name = "Projecto";

        protected bool ProjectoFolderExists()
        {
            String path = Path.Combine(folder_parent, folder_name);
            return Directory.Exists(path);
        }

        protected void CreateProjectoFolder()
        {
            String path = Path.Combine(folder_parent, folder_name);
            Directory.CreateDirectory(path);
        }

        protected String GetPath()
        {
            return Path.Combine(folder_parent, folder_name);
        }

        public void DestroyAll()
        {
            DirectoryInfo di = new DirectoryInfo(GetPath());

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
