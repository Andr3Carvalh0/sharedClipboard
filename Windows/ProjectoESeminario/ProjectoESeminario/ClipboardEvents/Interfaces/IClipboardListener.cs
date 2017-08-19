using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ProjectoESeminario
{
    public interface IClipboardListener
    {
        void enableService();
        void disableService();
        void updateClipboard(String text);

    }
}
