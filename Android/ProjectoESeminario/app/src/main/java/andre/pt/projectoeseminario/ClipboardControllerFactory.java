package andre.pt.projectoeseminario;

import andre.pt.projectoeseminario.State.ClipboardController;


public class ClipboardControllerFactory {
    private static ClipboardController clipboardController;


    public static ClipboardController getSingleton(String d){
        if(clipboardController == null)
            clipboardController = new ClipboardController(d);

        return clipboardController;
    }

}
