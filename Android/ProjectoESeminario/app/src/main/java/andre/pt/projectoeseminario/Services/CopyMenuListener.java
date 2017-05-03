package andre.pt.projectoeseminario.Services;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import andre.pt.projectoeseminario.Preferences;

public class CopyMenuListener extends Service {
    private Context context;
    private int userToken;

    @Override
    public void onCreate() {
        context = this;
        userToken = new Preferences(context).getIntPreference(Preferences.USER_TOKEN);

        ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

                if (clipboardManager.hasPrimaryClip()) {
                    ClipData.Item clipboardItem = clipboardManager.getPrimaryClip().getItemAt(0);
                    //For now we only support text
                    if (clipboardItem.getText() != null) {
                        Intent intent = new Intent(context, DownloadService.class);

                        intent.setAction("andre.pt.projectoeseminario.COPY_ACTION");
                        intent.putExtra("token", userToken);
                        intent.putExtra("text", clipboardItem.getText() + "");
                        startService(intent);
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
