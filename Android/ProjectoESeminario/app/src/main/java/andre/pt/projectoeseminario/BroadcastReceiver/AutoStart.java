package andre.pt.projectoeseminario.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import andre.pt.projectoeseminario.Adapters.Entities.Preference;
import andre.pt.projectoeseminario.Preferences;
import andre.pt.projectoeseminario.Services.CopyMenuListener;

public class AutoStart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(new Preferences(context).getBooleanPreference(Preferences.SERVICERUNNING)) {
            Intent i = new Intent(context, CopyMenuListener.class);
            context.startService(i);
        }
    }
}
