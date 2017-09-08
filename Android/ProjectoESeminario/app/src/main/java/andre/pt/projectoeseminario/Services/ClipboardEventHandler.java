package andre.pt.projectoeseminario.Services;

import android.app.IntentService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import java.util.HashMap;
import java.util.function.BiConsumer;
import andre.pt.projectoeseminario.Controller.Preferences;
import andre.pt.projectoeseminario.Projecto;
import andre.pt.projectoeseminario.Controller.API.APIRequest;
import andre.pt.projectoeseminario.Controller.State.ClipboardController;


/**
 * Handles what to do when, the firebase service receives an notification or we copied some value.
*/
public class ClipboardEventHandler extends Service {
    private static final String TAG = "Portugal:ClipHandler";
    private HashMap<String, BiConsumer<Intent, ClipboardController>> action_router;

    public ClipboardEventHandler() {
        super("ClipboardEventHandler");
    }

    /**
     * Stores the text into the device's clipboard
     *
     * @param context The application context
     * @param content The text value to store into the device's clipboard
    */
    private void handleTextContent(Context context, String content) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(content, content);
        clipboard.setPrimaryClip(clip);

    }

    /**
     * Handles a logout request.
     *
     * @param intent
     * @param clipboardController
     */
    private void handleRemove(Intent intent, ClipboardController clipboardController){
        ((Projecto)getApplication()).logOut();
    }

    /**
     * Stores the new value into the clipboard or into the server depending of
     * the upload state that is contained in the intent.
     *
     * @param intent The intent used to call the service
     * @param clipboardController Class to help "switch the value" on our application
     */
    private void handleStore(Intent intent, ClipboardController clipboardController){
        final Preferences preferences = new Preferences(getApplicationContext());

        //The only way to disable the firebase service
        if(!preferences.getBooleanPreference(Preferences.SERVICERUNNING))
            return;

        final String content = intent.getStringExtra("content");
        final int order = intent.getIntExtra("order", 0);
        final boolean isMIME = intent.getBooleanExtra("isMIME", false);
        final boolean upload = intent.getBooleanExtra("upload", false);
        final String token = intent.getStringExtra("token");
        final String device = ((Projecto)getApplication()).getDevice();

        if(upload){
            onCopy(content, token, device, clipboardController);
            return;
        }

        if(!isMIME)
            onReceived(content, order, clipboardController);
    }

    private void onCopy(String text, String user, String device, ClipboardController clipboardController){
    	Context ctx = this;
    	new Thread(() -> {
	        boolean[] hasResp = {false};

	        APIRequest mApi = new APIRequest(ctx);
	        try{
	            if(clipboardController.putValue(text, (pair) ->
	                    mApi.pushTextInformation(user,
	                            text,
	                            device,
	                            o -> {
	                                clipboardController.updateStateOfUpload(Long.parseLong((String)o));
	                                hasResp[0] = true;

	                            },
	                            () -> {
	                                clipboardController.removeUpload(pair);
	                                hasResp[0] = true;
	                            })
	            )){
	                ((Projecto)getApplication()).storeContent(text);
	            }}finally {
	                clipboardController.wake();
	            }
	        while (!hasResp[0]){}
	   
	    }).run();
    }


    private void onReceived(String text, int order, ClipboardController clipboardController){
        int value = clipboardController.PutValue(text, order);

        if (value == 1)
        {
            handleTextContent(getApplicationContext(), text);
            ((Projecto)getApplication()).storeContent(text);
            return;
        }

        if(value == 2)
            ((Projecto)getApplication()).storeContent(text);

    }

    //Called when we copy from history
    private void handleSwitch(Intent intent, ClipboardController clipboardController) {
        final String content = intent.getStringExtra("content");

        //This one to let
        clipboardController.addToIgnoreQueue(content);

        handleTextContent(getApplicationContext(), content);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final ClipboardController clipboardController = ((Projecto)getApplication()).getClipboardController();

        action_router = new HashMap<>();
        action_router.put("remove", this::handleRemove);
        action_router.put("store", this::handleStore);
        action_router.put("switch", this::handleSwitch);

        assert intent != null;
        final String action = intent.getStringExtra("action");
        final BiConsumer consumer = action_router.get(action);

        try {
            consumer.accept(intent, clipboardController);
        }catch (Exception e){
            //Nothing to do. Invalid request
        }

        //In case of low memory tells the OS to not bother to recreate this service
        return START_NOT_STICKY;
    }
}
