package andre.pt.projectoeseminario.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import andre.pt.projectoeseminario.Services.ClipboardWatcher;

public class AutoStart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, ClipboardWatcher.class);
        context.startService(i);
    }
}