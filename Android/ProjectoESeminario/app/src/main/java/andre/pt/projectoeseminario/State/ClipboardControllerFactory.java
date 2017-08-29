package andre.pt.projectoeseminario.State;


/**
 * Factory so we return always the same object of the ClipboardController class
 */
public class ClipboardControllerFactory {
    private static ClipboardController clipboardController;

    public static ClipboardController getSingleton(String data){
        if(clipboardController == null)
            clipboardController = new ClipboardController(data);

        return clipboardController;
    }

}
