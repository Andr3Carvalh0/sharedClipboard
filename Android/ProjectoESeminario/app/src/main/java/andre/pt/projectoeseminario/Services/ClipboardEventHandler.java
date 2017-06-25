package andre.pt.projectoeseminario.Services;

import android.app.IntentService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import andre.pt.projectoeseminario.ClipboardControllerFactory;
import andre.pt.projectoeseminario.Data.APIRequest;
import andre.pt.projectoeseminario.State.ClipboardController;

/*
* Handles what to do when, the firebase service receives an notification
*/
public class ClipboardEventHandler extends IntentService {

    private static final String TAG = "Portugal:ClipHandler";

    public ClipboardEventHandler(){
        this("ClipboardEventHandler");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ClipboardEventHandler(String name) {
        super(name);
    }

    /*
    *   Stores the text into the device clipboard
    */
    private void handleTextContent(Context context, String content) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(content, content);
        clipboard.setPrimaryClip(clip);
    }

    /*
    *   Stores the image into the device clipboard
    */
    private void handleMultimediaContent(Context context, String content) {

    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final ClipboardController clipboardController = ClipboardControllerFactory.getSingleton();
        final Context context = this.getApplicationContext();

        assert intent != null;
        final String content = intent.getStringExtra("content");
        final boolean isMIME = intent.getBooleanExtra("isMIME", false);
        final boolean upload = intent.getBooleanExtra("upload", false);
        final int token = intent.getIntExtra("token", 0);
        final String deviceID = intent.getStringExtra("deviceID");

        if(clipboardController.switchClipboardValue(content)){
            Log.v(TAG, "onHandleIntent");

            new Handler().post(() -> {
                while(!clipboardController.acquireWork()){
                    Log.v(TAG, "waiting...");
                }

                if(upload){
                    APIRequest mApi = new APIRequest(null, context);
                    mApi.pushTextInformation(token, content, deviceID);
                }else{
                    if (isMIME) {
                        handleMultimediaContent(context, content);
                        return;
                    }
                    handleTextContent(context, content);
                }

                clipboardController.releaseWork();
            });
        }
    }
}
