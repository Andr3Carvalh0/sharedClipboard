package andre.pt.projectoeseminario.Services;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;
public class PasteMenuListener extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_LONG_CLICKED){
            Intent intent = new Intent(this, DownloadService.class);

            int user_token = getUserToken();
            intent.setAction("andre.pt.projectoeseminario.PASTE_ACTION");
            intent.putExtra("token", user_token);
            startService(intent);
            Toast.makeText(this, "Imagine that we are fetching info.", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onInterrupt() {
    }

    private int getUserToken(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        return preferences.getInt("user_token", 0);
    }

}
