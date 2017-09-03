using ProjectoESeminario.Controller.Communication.Utils;
using System;

namespace ProjectoESeminario.Controller.Data.Cache.Interface
{
    public interface ICache
    {
        void Store(String text);
        void Store(ImageEx image);
        String[] Pull(String category);
        String[] Pull();
        void FlushAll();
    }
}
