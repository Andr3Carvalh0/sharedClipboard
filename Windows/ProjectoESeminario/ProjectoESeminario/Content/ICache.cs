using ProjectoESeminario.DTOs;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ProjectoESeminario.Content
{
    public interface ICache
    {
        void store(String text);
        void store(ImageEx image);
        String[] pull(String category);
        String[] pull();
        void flushAll();
    }
}
