using System;

namespace ProjectoESeminario.Controller.State
{
    public class ClipboardControllerFactory
    {
        private static ClipboardController clipboardController;


        public static ClipboardController getSingleton(String data)
        {
            return clipboardController ?? (clipboardController = new ClipboardController(data));
        }

    }
}
