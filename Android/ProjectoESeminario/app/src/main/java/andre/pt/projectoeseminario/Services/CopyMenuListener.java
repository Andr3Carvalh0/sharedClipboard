package andre.pt.projectoeseminario.Services;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import andre.pt.projectoeseminario.Adapters.Entities.Preference;
import andre.pt.projectoeseminario.Preferences;


/*
* Service responsable for creating the clipboard listener, and handle the change of the same
*/
public class CopyMenuListener extends Service {
    private int userToken;
    private String deviceID;
    public static final String TAG = "Portugal:CopyMenu";

    @Override
    public void onCreate() {
        Preferences pref = new Preferences(this);

        userToken = pref.getIntPreference(Preferences.USER_TOKEN);
        deviceID = pref.getStringPreference(Preferences.FIREBASEID);

        //We cannot update when the user token isnt valid or when we cannot get the copied text
        if(userToken == 0)
            return;

        final Context ctx = this;

        ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).addPrimaryClipChangedListener(() -> {
            try{
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

                if (clipboardManager.hasPrimaryClip()) {
                    ClipData.Item clipboardItem = clipboardManager.getPrimaryClip().getItemAt(0);

                    //For now we only support text
                    if (clipboardItem.getText() != null) {
                        String text = clipboardItem.getText() + "";

                        Log.v(TAG, "Uploading text to server");
                        Intent intent = new Intent(ctx, ClipboardEventHandler.class);
                        intent.putExtra("content", text);
                        intent.putExtra("deviceID", deviceID);
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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
