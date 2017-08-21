using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

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
    }
}
