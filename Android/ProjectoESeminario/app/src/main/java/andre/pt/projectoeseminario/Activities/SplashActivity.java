package andre.pt.projectoeseminario.Activities;

import android.content.Intent;
import android.os.Handler;

import andre.pt.projectoeseminario.R;

public class SplashActivity extends ParentActivity {

    @Override
    protected void init() {
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent it;

                if(hasCompletedSetup()){
                    it = new Intent(getBaseContext(), PreferencesActivity.class);
                    it.putExtra("token", getIntPreference("user_token"));
                }else{
                    it = new Intent(getApplicationContext(), LoginActivity.class);
                }

                startActivity(it);
                finish();
            }
        }, 1000);
    }

    @Override
    protected void setupEvents() {

    }
}
