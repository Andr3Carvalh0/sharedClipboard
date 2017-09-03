using ProjectoESeminario.Controller.Data.Cache.Interface;

namespace ProjectoESeminario.Controller.Data.Cache
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
