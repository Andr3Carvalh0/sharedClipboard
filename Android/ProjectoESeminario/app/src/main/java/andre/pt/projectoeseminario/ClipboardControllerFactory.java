package andre.pt.projectoeseminario;

import andre.pt.projectoeseminario.State.ClipboardController;


public class ClipboardControllerFactory {
    private static ClipboardController clipboardController;


    public static ClipboardController getSingleton(){
        if(clipboardController == null)
            clipboardController = new ClipboardController();

        return clipboardController;
    }

}
