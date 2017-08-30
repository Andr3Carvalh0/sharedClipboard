using ProjectoESeminario.DTOs;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Runtime.CompilerServices;

namespace ProjectoESeminario.Content
{
    public class Cache : ICache
    {
        private Databases.TextHandler textHandler;
        private Databases.FileHandler fileHandler;
        private ConditionalWeakTable<String, LinkedList<String>> textCache;
        private LinkedList<String> imageCache;
        private Object nLock;

        public Cache()
        {
            this.textHandler = new Databases.TextHandler();
            this.fileHandler = new Databases.FileHandler();
            this.textCache = new ConditionalWeakTable<String, LinkedList<String>>();
            this.imageCache = new LinkedList<String>();
            this.nLock = new object();
        }

        public string[] pull(string category)
        {
            lock (nLock) { 
                LinkedList<String> elems;
                textCache.TryGetValue(category, out elems);

                if(elems == null)
                    elems = textHandler.fetch(category);

                return elems.ToArray();
            }
        }

        public String[] pull()
        {
            lock (nLock)
            {
                if (imageCache == null)
                    imageCache = fileHandler.fetch();

                return imageCache.ToArray();
            }
        }

        public void store(string text)
        {
            lock (nLock)
            {
                String category = Classifiers.Classifiers.TEXT_CATEGORY;

                if (Classifiers.Classifiers.isContact(text))
                    category = Classifiers.Classifiers.CONTACTS_CATEGORY;

                if (Classifiers.Classifiers.isLink(text))
                    category = Classifiers.Classifiers.LINKS_CATEGORY;

                textHandler.store(text, category);

                try
                {
                    textCache.Remove(category);
                    textCache.Add(category, textHandler.fetch(category));
                }
                catch (Exception)
                {
                    textCache.Add(category, textHandler.fetch(category));
                }
            }
        }

        public void store(ImageEx image)
        {
            lock (nLock)
            {
                fileHandler.store(image);
                imageCache = fileHandler.fetch();
            }
        }

        public void flushAll()
        {
            lock (nLock)
            {
                fileHandler.destroyAll();
                textHandler.destroyAll();
            }
        }
    }
}
