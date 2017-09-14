package andre.pt.projectoeseminario.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import andre.pt.projectoeseminario.Controller.Preferences;
import andre.pt.projectoeseminario.Services.CopyMenuListener;
import andre.pt.projectoeseminario.Services.FirebaseMessageHandler;


/**
 * Broadcast that start the listening service when the device boots up
*/
public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(new Preferences(context).getBooleanPreference(Preferences.SERVICERUNNING)) {
            Intent i = new Intent(context, CopyMenuListener.class);
            context.startService(i);

            i = new Intent(context, FirebaseMessageHandler.class);
            context.startService(i);
        }
    }
}
