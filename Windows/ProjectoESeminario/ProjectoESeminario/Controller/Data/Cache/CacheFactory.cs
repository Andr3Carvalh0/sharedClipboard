using ProjectoESeminario.Controller.Data.Cache.Interface;

namespace ProjectoESeminario.Controller.Data.Cache
{
    public static class CacheFactory
    {
        private static ICache cache = null;

        public static ICache getCache()
        {
            return cache ?? (cache = new Cache());
        }

    }
}
