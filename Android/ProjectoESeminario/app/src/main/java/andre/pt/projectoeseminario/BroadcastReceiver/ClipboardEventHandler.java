package andre.pt.projectoeseminario.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import andre.pt.projectoeseminario.ClipboardControllerFactory;
import andre.pt.projectoeseminario.Data.APIRequest;
import andre.pt.projectoeseminario.State.ClipboardController;

/*
* Handles what to do when, the firebase service receives an notification
*/
public class ClipboardEventHandler extends BroadcastReceiver {

    private static final String TAG = "Portugal:ClipHandler";

    @Override
    public void onReceive(Context context, Intent intent) {
        final ClipboardController clipboardController = ClipboardControllerFactory.getSingleton();

        final String content = intent.getStringExtra("content");
        final boolean isMIME = intent.getBooleanExtra("isMIME", false);
        final boolean upload = intent.getBooleanExtra("upload", false);
        final int token = intent.getIntExtra("token", 0);

        Log.v(TAG, "onReceive");
        if(upload){
            if(clipboardController.releaseWork()) {
                new Thread(() -> {
                    APIRequest mApi = new APIRequest(null, context);
                    mApi.pushTextInformation(token, content);
                });
            }

        }else{
            clipboardController.acquireWork();

            if(isMIME){
                handleMultimediaContent(context, content);
                return;
            }

            handleTextContent(context, content);

        }
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


}
