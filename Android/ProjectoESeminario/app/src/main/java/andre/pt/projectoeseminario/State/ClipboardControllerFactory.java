package andre.pt.projectoeseminario.State;


public class ClipboardControllerFactory {
    private static ClipboardController clipboardController;


    public static ClipboardController getSingleton(String data){
        if(clipboardController == null)
            clipboardController = new ClipboardController(data);

        return clipboardController;
    }

}
