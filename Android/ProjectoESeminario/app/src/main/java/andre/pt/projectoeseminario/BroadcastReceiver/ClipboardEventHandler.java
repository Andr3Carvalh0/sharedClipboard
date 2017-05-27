package andre.pt.projectoeseminario.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ClipboardEventHandler extends BroadcastReceiver {


    private static final String TAG = "Portugal:ClipHandler";

    @Override
    public void onReceive(Context context, Intent intent) {
        final String content = intent.getStringExtra("content");
        final boolean isMIME = intent.getBooleanExtra("isMIME", false);

        Log.v(TAG, "onReceive");
        
        if(isMIME){
            handleMultimediaContent(context, content);
            return;
        }

        handleTextContent(context, content);
    }

    private void handleTextContent(Context context, String content) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(content, content);
        clipboard.setPrimaryClip(clip);
    }

    private void handleMultimediaContent(Context context, String content) {

    }
}
