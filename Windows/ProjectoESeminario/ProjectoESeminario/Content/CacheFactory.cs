using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ProjectoESeminario.Content
{
    public static class CacheFactory
    {
        private static ICache cache = null;

        public static ICache getCache()
        {
            if(cache == null)
                cache = new Cache();

            return cache;
        }

    }
}
