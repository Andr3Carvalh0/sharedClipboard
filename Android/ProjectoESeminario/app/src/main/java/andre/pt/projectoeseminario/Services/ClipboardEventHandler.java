package andre.pt.projectoeseminario.Services;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

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
        action_router = new HashMap<>();
        action_router.put("remove", this::handleRemove);
        action_router.put("store", this::handleStore);
        action_router.put("switch", this::handleSwitch);

    }


    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println();
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
    public int onStartCommand(Intent intent, int flags, int startId) {
        final ClipboardController clipboardController = ((Projecto)getApplication()).getClipboardController();

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
        APIRequest mApi = new APIRequest(getApplicationContext());

        try {
            if (clipboardController.putValue(content)) {
                Log.v(TAG, "onHandleIntent");

                //Came from device (locally)
                if (upload) {
                    ((Projecto)getApplication()).storeContent(content);
                    mApi.pushTextInformation(token, content, this::stopSelf);
                    return;
                }

                //Came from the server
                if (!isMIME) {
                    ((Projecto)getApplication()).storeContent(content);
                    handleTextContent(getApplicationContext(), content);
                    return;
                }
            }
        } catch (InterruptedException e) {

        } finally {
            clipboardController.wake();

            if(!upload)
                this.stopSelf();
        }
    }

    private void handleSwitch(Intent intent, ClipboardController clipboardController) {
        final String content = intent.getStringExtra("content");

        try {
            if (clipboardController.putValue(content)) {
                Log.v(TAG, "onHandleIntent");

                handleTextContent(getApplicationContext(), content);
            }
        } catch (InterruptedException e) {

        } finally {
            clipboardController.wake();
            this.stopSelf();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
