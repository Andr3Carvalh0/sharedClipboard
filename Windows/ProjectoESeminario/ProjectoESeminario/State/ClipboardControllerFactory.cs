using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ProjectoESeminario.State
{
    public class ClipboardControllerFactory
    {
        private static ClipboardController clipboardController;


        public static ClipboardController getSingleton(String data)
        {
            if (clipboardController == null)
                clipboardController = new ClipboardController(data);

            return clipboardController;
        }

    }
}
