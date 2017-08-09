package andre.pt.projectoeseminario.Services;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import andre.pt.projectoeseminario.Services.ClipboardEventHandler;

public class FirebaseMessageHandler extends FirebaseMessagingService {

    private final String TAG = "Portugal:FirebaseMessag";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            final Boolean isMIME = Boolean.valueOf(remoteMessage.getData().get("isMIME"));
            final String content = remoteMessage.getData().get("content");

            Intent intent = new Intent(this, ClipboardEventHandler.class);
            intent.putExtra("content", content);
            intent.putExtra("isMIME", isMIME);

            startService(intent);

        }
    }



}
