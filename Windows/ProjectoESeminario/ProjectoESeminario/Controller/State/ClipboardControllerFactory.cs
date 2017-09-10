using System;

namespace ProjectoESeminario.Controller.State
{
    public class ClipboardControllerFactory
    {
        private static ClipboardController clipboardController;


        public static ClipboardController getSingleton(long order)
        {
            return clipboardController ?? (clipboardController = new ClipboardController(order));
        }

    }
}
