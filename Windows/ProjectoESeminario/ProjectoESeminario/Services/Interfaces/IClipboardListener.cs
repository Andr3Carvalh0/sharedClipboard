using System;
using System.Drawing;

namespace ProjectoESeminario.Services.Interfaces
{
    public interface IClipboardListener
    {
        void UpdateClipboard(string text);
        void UpdateClipboard(string path, Image image);
        void Start();
        void Stop();
    }
}
