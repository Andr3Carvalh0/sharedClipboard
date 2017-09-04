using ProjectoESeminario.Controller.Communication.Utils;
using ProjectoESeminario.Controller.Data.Cache.Interface;
using ProjectoESeminario.Controller.Data.Database;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.CompilerServices;

namespace ProjectoESeminario.Controller.Data.Cache
{
    public class Cache : ICache
    {
        private readonly TextHandler textHandler;
        private readonly FileHandler fileHandler;
        private readonly ConditionalWeakTable<String, LinkedList<String>> textCache;
        private LinkedList<String> imageCache;
        private readonly Object nLock;

        public Cache()
        {
            this.textHandler = new TextHandler();
            this.fileHandler = new FileHandler();
            this.textCache = new ConditionalWeakTable<String, LinkedList<String>>();
            this.nLock = new object();
        }

        public string[] Pull(string category)
        {
            lock (nLock) {
                textCache.TryGetValue(category, out var elems);

                if(elems == null)
                    elems = textHandler.Fetch(category);

                return elems.ToArray();
            }
        }

        public String[] Pull()
        {
            lock (nLock)
            {
                if (imageCache == null)
                    imageCache = fileHandler.Fetch();

                return imageCache.ToArray();
            }
        }

        public void Store(string text)
        {
            lock (nLock)
            {
                String category = Classifiers.Classifiers.TEXT_CATEGORY;

                if (Classifiers.Classifiers.isContact(text))
                    category = Classifiers.Classifiers.CONTACTS_CATEGORY;

                if (Classifiers.Classifiers.isLink(text))
                    category = Classifiers.Classifiers.LINKS_CATEGORY;

                textHandler.Store(text, category);

                try
                {
                    textCache.Remove(category);
                    textCache.Add(category, textHandler.Fetch(category));
                }
                catch (Exception)
                {
                    textCache.Add(category, textHandler.Fetch(category));
                }
            }
        }

        public void Store(ImageEx image)
        {
            lock (nLock)
            {
                fileHandler.Store(image);
                imageCache = fileHandler.Fetch();
            }
        }

        public void FlushAll()
        {
            lock (nLock)
            {
                fileHandler.DestroyAll();
                textHandler.DestroyAll();
            }
        }
    }
}
