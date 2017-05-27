package andre.pt.projectoeseminario.Activities;

import android.app.ActivityManager;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.CompoundButton;
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.HashMap;
import andre.pt.projectoeseminario.Adapters.Entities.Preference;
import andre.pt.projectoeseminario.Adapters.PreferencesAdapter;
import andre.pt.projectoeseminario.Data.APIRequest;
import andre.pt.projectoeseminario.Firebase.FirebaseMessageHandler;
import andre.pt.projectoeseminario.Preferences;
import andre.pt.projectoeseminario.R;
import andre.pt.projectoeseminario.Services.CopyMenuListener;

public class PreferencesActivity extends ParentActivity{

    private final String TAG = "Portugal:Preferences";
    private boolean service_state;
    private int user;
    private FirebaseMessageHandler firebaseMessageHandler;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void init() {
        setContentView(R.layout.activity_preferences);
        firebaseMessageHandler = new FirebaseMessageHandler();



        //Setup preferences
        user = getIntPreference(Preferences.USER_TOKEN);
        service_state = getBooleanPreference(Preferences.SERVICERUNNING);

        String firebaseID = FirebaseInstanceId.getInstance().getToken();
        String tmp = getStringPreference(Preferences.FIREBASEID);

        //On this case its our first launch
        if(tmp == null)
            service_state = true;

        if(tmp == null || !tmp.equals(firebaseID))
            handleNewFirebaseID(user, firebaseID);


        Preference[] preferences = new Preference[]{new Preference(getString(R.string.Service_Running_Option_Title), getString(R.string.Service_Running_Option_Description), service_state)};
        HashMap<String, CompoundButton.OnCheckedChangeListener> preferencesActions = new HashMap<>();
        preferencesActions.put(getString(R.string.Service_Running_Option_Title), new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    stopService();
                }else{
                    startService();
                }

                //Why doing this here?Because on Pause/OnDestroy is not reliable.
                saveBooleanPreference(Preferences.SERVICERUNNING, isChecked);
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        PreferencesAdapter adapter = new PreferencesAdapter(preferences, preferencesActions);

        recyclerView.setAdapter(adapter);

        toolbar.setTitle(getString(R.string.app));

    }

    private void handleNewFirebaseID(long token, String firebaseID) {
        try {
            new APIRequest(null, this).registerDevice(token, firebaseID);
            saveStringPreference(Preferences.FIREBASEID, firebaseID);
        }catch (Exception e){
            Log.v(TAG, "Cannot communicate with server right now!");
        }
    }

    @Override
    protected void setupEvents() {
        if(service_state)
            startService();
        else
            stopService();

        Log.d(TAG, "is Service Running: " + isServiceRunning());

        setSupportActionBar(toolbar);

    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if(CopyMenuListener.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void startService(){
        if(!isServiceRunning()){
            startService(new Intent(this, CopyMenuListener.class));
        }
    }

    private void stopService(){
        stopService(new Intent(this, CopyMenuListener.class));


    }

}
