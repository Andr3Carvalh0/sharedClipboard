using System;
using System.Drawing;

namespace ProjectoESeminario.Services.Interfaces
{
    public interface IClipboardListener
    {
        void UpdateClipboard(String text);
        void UpdateClipboard(Image image);
        void Start();
        void Stop();
    }
}
