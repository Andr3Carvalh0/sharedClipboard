package andre.pt.projectoeseminario;

import android.app.Application;
import android.content.Intent;

public class Projecto extends Application {
    private boolean visible = true;

    public void logOut(){
        final Preferences preferences = new Preferences(getApplicationContext());
        preferences.clearAll();
        preferences.saveBooleanPreference(Preferences.SERVICERUNNING, false);

        if(visible) {
            Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }

    public void setVisible(){
        visible = true;
    }

    public void setInvisible(){
        visible = false;
    }

}
