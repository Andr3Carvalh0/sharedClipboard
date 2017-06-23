package andre.pt.projectoeseminario.Activities;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import andre.pt.projectoeseminario.Activities.Abstract.ParentActivity;
import andre.pt.projectoeseminario.Preferences;
import andre.pt.projectoeseminario.R;

/*
* Splash Activity. Used to decide which activity to launch
*/
public class SplashActivity extends ParentActivity {

    private static final String TAG = "Portugal:Splash";

    @Override
    protected void init() {
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            Intent it;

            if(hasCompletedSetup()){
                Log.v(TAG, "Loading preferences activity");
                it = new Intent(getBaseContext(), SettingsActivity.class);
                it.putExtra("token", getIntPreference(Preferences.USER_TOKEN));
            }else{
                Log.v(TAG, "Loading login activity");
                it = new Intent(getApplicationContext(), LoginActivity.class);
            }

            startActivity(it);
            finish();
        }, 1000);
    }

    @Override
    protected void setupEvents() {

    }
}
