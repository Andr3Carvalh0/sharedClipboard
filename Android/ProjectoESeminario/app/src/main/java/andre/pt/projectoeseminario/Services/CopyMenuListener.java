package andre.pt.projectoeseminario.Services;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import andre.pt.projectoeseminario.Data.APIRequest;
import andre.pt.projectoeseminario.Preferences;

public class CopyMenuListener extends Service {
    private int userToken;
    public final String TAG = "Portugal:CopyMenu";
    private APIRequest mApi;

    @Override
    public void onCreate() {
        mApi = new APIRequest(null, this);
        userToken = new Preferences(this).getIntPreference(Preferences.USER_TOKEN);

        //We cannot update when the user token isnt valid or when we cannot get the copied text
        if(userToken == 0)
            return;

        ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {

            @Override
            public void onPrimaryClipChanged() {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

                if (clipboardManager.hasPrimaryClip()) {
                    ClipData.Item clipboardItem = clipboardManager.getPrimaryClip().getItemAt(0);

                    //For now we only support text
                    if (clipboardItem.getText() != null) {
                        String text = clipboardItem.getText() + "";

                        Log.v(TAG, "Uploading text to server");
                        mApi.pushTextInformation(userToken, text);

                    }
                }
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
