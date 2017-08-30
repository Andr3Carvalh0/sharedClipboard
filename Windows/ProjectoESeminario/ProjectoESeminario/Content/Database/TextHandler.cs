using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text.RegularExpressions;
using System.Threading;

namespace ProjectoESeminario.Databases
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
            dbs.Add(Classifiers.Classifiers.TEXT_CATEGORY, (s1) => add(Classifiers.Classifiers.TEXT_CATEGORY, s1));
            dbs.Add(Classifiers.Classifiers.LINKS_CATEGORY, (s1) => add(Classifiers.Classifiers.LINKS_CATEGORY, s1));
            dbs.Add(Classifiers.Classifiers.CONTACTS_CATEGORY, (s1) => add(Classifiers.Classifiers.CONTACTS_CATEGORY, s1));
        }

        /// <summary>
        /// Stores the content text into the soon to be classifed category
        /// </summary>
        /// <param name="text">The copied text</param>
        public void store(String text, String category) {
            if (!projectoFolderExists())
                createProjectoFolder();

            evaluateCleanup();

            try
            {
                Action<String> action;
                dbs.TryGetValue(category, out action);

                action.Invoke(text);
            }
            catch (Exception)
            {
                //Well, then fuck off...
            }
        }

        public LinkedList<String> fetch(String category)
        {
            try {
                String[] file = File.ReadAllLines(Path.Combine(getPath(), category + ".txt"));

                return process(file);

            }
            catch (Exception)
            {
                return new LinkedList<string>();
            }
        }

        private LinkedList<String> process(String[] text)
        {
            LinkedList<String> lines = new LinkedList<string>();

            String line_tmp = "";
            
            for (int i = 0; i < text.Length; i++)
            {
                if (!text[i].Equals(delimiter)) { 
                    line_tmp += text[i] + "\n";
                }
                else { 
                    lines.AddLast(line_tmp);
                    line_tmp = "";
                }
            }

            return lines;
        }

        private void cleanUP(string db)
        {
            File.Delete(Path.Combine(getPath(), db + ".txt"));
        }

        private void evaluateCleanup()
        {
            for(int i = 0; i < dbs_name.Length; i++)
            {
                try { 
                    if (new FileInfo(Path.Combine(getPath(), dbs_name[i] + ".txt")).Length > file_max_size)
                        cleanUP(dbs_name[i]);
                }
                catch (FileNotFoundException)
                {

                }
            }
        }

        private void add(string db, string text)
        { 
            using (StreamWriter sw = File.AppendText(Path.Combine(getPath(), db + ".txt")))
            {
                sw.WriteLine(text);
                sw.WriteLine(delimiter);
            }
        }
    }
}
