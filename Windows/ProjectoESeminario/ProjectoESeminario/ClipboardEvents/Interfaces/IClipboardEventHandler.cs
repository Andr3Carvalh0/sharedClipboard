using System;
using System.Collections.Generic;

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
