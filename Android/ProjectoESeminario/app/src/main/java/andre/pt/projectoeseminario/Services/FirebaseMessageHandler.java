package andre.pt.projectoeseminario.Services;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Service that handles all the messages that come from the firebase server
 */
public class FirebaseMessageHandler extends FirebaseMessagingService {
    private final String TAG = "Portugal:FirebaseMessag";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            String action = String.valueOf(remoteMessage.getData().get("action"));
            final Boolean isMIME = Boolean.valueOf(remoteMessage.getData().get("isMIME"));
            final String content = remoteMessage.getData().get("content");
            int order = 0;

            try{
                order = Integer.parseInt(remoteMessage.getData().get("order"));
            }catch (Exception e){}

            Intent intent = new Intent(this, ClipboardEventHandler.class);
            intent.putExtra("action", action);
            intent.putExtra("content", content);
            intent.putExtra("isMIME", isMIME);
            intent.putExtra("order", order);


            startService(intent);
        }
    }
}
