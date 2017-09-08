package andre.pt.projectoeseminario.Services;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import andre.pt.projectoeseminario.Controller.Preferences;


/**
 * Service responsible for creating the clipboard listener, and handle the change of the same
*/
public class CopyMenuListener extends Service {
    private String userToken;
    private String deviceID;
    public static final String TAG = "Portugal:CopyMenu";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Preferences pref = new Preferences(this);

        userToken = pref.getStringPreference(Preferences.USER_TOKEN);
        deviceID = pref.getStringPreference(Preferences.FIREBASEID);

        final Context ctx = this;

        ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).addPrimaryClipChangedListener(() -> {
            try{
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

                if (clipboardManager.hasPrimaryClip()) {
                    ClipData.Item clipboardItem = clipboardManager.getPrimaryClip().getItemAt(0);

                    if (clipboardItem.getText() != null) {
                        String text = clipboardItem.getText() + "";

                        Log.v(TAG, "Uploading text to server");
                        Intent intent = new Intent(ctx, ClipboardEventHandler.class);
                        intent.putExtra("action", "store");
                        intent.putExtra("content", text);
                        intent.putExtra("upload", true);
                        intent.putExtra("token", userToken);

                        startService(intent);
                    }
                }
            }catch(Exception e){
                Log.v(TAG, "Cannot communicate with the server");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println();
    }
}
