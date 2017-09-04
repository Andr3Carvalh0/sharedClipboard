using ProjectoESeminario.Controller.Data.Database.Interface;
using System;
using System.Collections.Generic;
using System.IO;

namespace ProjectoESeminario.Controller.Data.Database
{
    public class TextHandler : ParentHandler
    {
        private readonly Dictionary<String, Action<String>> dbs ;
        private readonly String[] dbs_name = { Classifiers.Classifiers.CONTACTS_CATEGORY, Classifiers.Classifiers.TEXT_CATEGORY, Classifiers.Classifiers.LINKS_CATEGORY };
        private readonly long file_max_size = 5000000; // 5mb
        private const String delimiter = "<[/[A-C]/]>";

        public TextHandler()
        {
            this.dbs = new Dictionary<string, Action<string>>();
            dbs.Add(Classifiers.Classifiers.TEXT_CATEGORY, (s1) => Add(Classifiers.Classifiers.TEXT_CATEGORY, s1));
            dbs.Add(Classifiers.Classifiers.LINKS_CATEGORY, (s1) => Add(Classifiers.Classifiers.LINKS_CATEGORY, s1));
            dbs.Add(Classifiers.Classifiers.CONTACTS_CATEGORY, (s1) => Add(Classifiers.Classifiers.CONTACTS_CATEGORY, s1));
        }

        /// <summary>
        /// Stores the content text into the soon to be classifed category
        /// </summary>
        /// <param name="text">The copied text</param>
        /// <param name="category"></param>
        public void Store(String text, String category) {
            if (!ProjectoFolderExists())
                CreateProjectoFolder();

            EvaluateCleanup();

            try
            {
                dbs.TryGetValue(category, out var action);

                action.Invoke(text);
            }
            catch (Exception)
            {
                //Well, then fuck off...
            }
        }

        public LinkedList<String> Fetch(String category)
        {
            try {
                String[] file = File.ReadAllLines(Path.Combine(GetPath(), category + ".txt"));

                return Process(file);

            }
            catch (Exception)
            {
                return new LinkedList<string>();
            }
        }

        private LinkedList<String> Process(String[] text)
        {
            LinkedList<String> lines = new LinkedList<string>();

            String line_tmp = "";
            
            foreach (string t in text)
            {
                if (!t.Equals(delimiter)) { 
                    line_tmp += t + "\n";
                }
                else { 
                    lines.AddLast(line_tmp);
                    line_tmp = "";
                }
            }

            return lines;
        }

        private void CleanUp(string db)
        {
            File.Delete(Path.Combine(GetPath(), db + ".txt"));
        }

        private void EvaluateCleanup()
        {
            foreach (string t in dbs_name)
            {
                try { 
                    if (new FileInfo(Path.Combine(GetPath(), t + ".txt")).Length > file_max_size)
                        CleanUp(t);
                }
                catch (FileNotFoundException)
                {

                }
            }
        }

        private void Add(string db, string text)
        { 
            using (StreamWriter sw = File.AppendText(Path.Combine(GetPath(), db + ".txt")))
            {
                sw.WriteLine(text);
                sw.WriteLine(delimiter);
            }
        }
    }
}
