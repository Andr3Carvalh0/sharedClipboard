package andre.pt.projectoeseminario.Services;

import android.app.IntentService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import andre.pt.projectoeseminario.Classifiers.Classifiers;
import andre.pt.projectoeseminario.Preferences;
import andre.pt.projectoeseminario.Projecto;
import andre.pt.projectoeseminario.State.ClipboardControllerFactory;
import andre.pt.projectoeseminario.ContentProvider.ResourcesContentProviderContent;
import andre.pt.projectoeseminario.API.APIRequest;
import andre.pt.projectoeseminario.State.ClipboardController;


/**
 * Handles what to do when, the firebase service receives an notification or we copied some value.
*/
public class ClipboardEventHandler extends IntentService {
    private static final String TAG = "Portugal:ClipHandler";
    private final HashMap<String, BiConsumer<Intent, ClipboardController>> action_router;
    public ClipboardEventHandler() {
        this("ClipboardEventHandler");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ClipboardEventHandler(String name) {
        super(name);
        action_router = new HashMap<>();
        action_router.put("remove", this::handleRemove);
        action_router.put("store", this::handleStore);

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
     * Main method. Used to route to the correct action
     *
     * @param intent The intent used to call this service
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData.Item clipboardItem = clipboard.getPrimaryClip().getItemAt(0);
        final ClipboardController clipboardController = ClipboardControllerFactory.getSingleton((clipboardItem.getText() != null ? String.valueOf(clipboardItem.getText()) : "Welcome"));

        assert intent != null;
        final String action = intent.getStringExtra("action");
        final BiConsumer consumer = action_router.get(action);

        try {
            consumer.accept(intent, clipboardController);
        }catch (Exception e){
            //Nothing to do. Invalid request
        }
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
        final boolean isMIME = intent.getBooleanExtra("isMIME", false);
        final boolean upload = intent.getBooleanExtra("upload", false);
        final String token = intent.getStringExtra("token");
        APIRequest mApi = new APIRequest(null, getApplicationContext());

        try {
            if (clipboardController.putValue(content)) {
                Log.v(TAG, "onHandleIntent");

                if (upload) {
                    ((Projecto)getApplication()).storeContent(content);
                    mApi.pushTextInformation(token, content);
                    return;
                }

                if (!isMIME) {
                    ((Projecto)getApplication()).storeContent(content);
                    handleTextContent(getApplicationContext(), content);
                    return;
                }
            }
        } catch (InterruptedException e) {

        } finally {
            clipboardController.wake();
        }
    }
}
