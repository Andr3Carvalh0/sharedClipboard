package andre.pt.projectoeseminario.Activities;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import andre.pt.projectoeseminario.Activities.Interfaces.ParentActivity;
import andre.pt.projectoeseminario.Preferences;
import andre.pt.projectoeseminario.R;

/*
* Splash Activity. Used to decide which activity to launch
*/
public class SplashActivity extends ParentActivity {

    private static final String TAG = "Portugal:Splash";

    boolean DEBUG = true;

    @Override
    protected void init() {
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(() -> {
            Intent it;

            if(hasCompletedSetup() || DEBUG){
                Log.v(TAG, "Loading preferences activity");
                it = new Intent(getBaseContext(), SettingsActivity.class);
                it.putExtra("token", getIntPreference(Preferences.USER_TOKEN));
                startActivity(it);
            }else{
                if(accountIsValid() || DEBUG) {
                    Log.v(TAG, "Loading login activity");
                    it = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(it);
                }
                showDialogWithPositiveButton(getString(R.string.Error403_Title), getString(R.string.CANNOT_AUTHENTICATE), getString(R.string.PositiveButton_Title), (dialog, which) -> dialog.dismiss());
            }

            finish();
        }, 1000);
    }

    private boolean accountIsValid() {

        return false;
    }


    @Override
    protected void setupEvents() {

    }
}
