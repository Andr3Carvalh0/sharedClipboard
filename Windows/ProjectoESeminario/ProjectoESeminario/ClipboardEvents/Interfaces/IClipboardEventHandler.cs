using System;
using System.Collections.Generic;
using System.Drawing.Imaging;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ProjectoESeminario.ClipboardEvents
{
    public interface IClipboardEventHandler
    {
        void onCopy(String text);
        void onCopyMime(String path);

        void onReceive(String text);

        void startService();
        void stopService();


    }
}
