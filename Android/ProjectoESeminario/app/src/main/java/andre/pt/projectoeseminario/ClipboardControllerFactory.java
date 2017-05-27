package andre.pt.projectoeseminario;

import andre.pt.projectoeseminario.State.ClipboardController;

/**
 * Created by André Carvalho on 27/05/2017.
 */

public class ClipboardControllerFactory {
    private static ClipboardController clipboardController;


    public static ClipboardController getSingleton(){
        if(clipboardController == null)
            clipboardController = new ClipboardController();

        return clipboardController;
    }

}
