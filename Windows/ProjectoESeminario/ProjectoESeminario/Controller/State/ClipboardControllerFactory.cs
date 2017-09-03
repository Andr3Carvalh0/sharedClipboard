﻿using System;

namespace ProjectoESeminario.Controller.State
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