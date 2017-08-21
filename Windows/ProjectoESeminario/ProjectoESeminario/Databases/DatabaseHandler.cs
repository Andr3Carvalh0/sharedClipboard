using System;
using System.IO;

namespace ProjectoESeminario.Databases
{
    public class DatabaseHandler : ParentHandler
    {
        private readonly string[] dbs = { "text", "links", "contacts" };
        private readonly long file_max_size = 5000000; // 5mb

        public string store(String text) {
            if (!projectoFolderExists())
                createProjectoFolder();

            shouldWeCleanUP();

            if (Classifiers.Classifiers.isLink(text))
                return addLink(text);

            if (Classifiers.Classifiers.isContact(text))
                return addContact(text);

            return addText(text);
        }

        private void cleanUP(string db)
        {
            File.Delete(Path.Combine(getPath(), db + ".txt"));
        }

        private void shouldWeCleanUP()
        {
            for(int i = 0; i < dbs.Length; i++)
            {
                if (new FileInfo(Path.Combine(getPath(), dbs[i] + ".txt")).Length > file_max_size)
                    cleanUP(dbs[i]);
            }
        }

        private void add(string db, string text)
        {
            using (StreamWriter sw = File.AppendText(Path.Combine(getPath(), db + ".txt")))
            {
                sw.WriteLineAsync(text);
            }

        }

        private string addText(string text)
        {
            add(dbs[0], text);
            //we return a string so that the if only has 1 line
            return "";
        }

        private string addLink(string text)
        {
            add(dbs[1], text);
            return "";
        }

        private string addContact(string text)
        {
            add(dbs[2], text);
            return "";
        }
    }
}
