package andre.pt.projectoeseminario.Activities;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
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
import andre.pt.projectoeseminario.Adapters.Settings.PreferencesAdapter;
import andre.pt.projectoeseminario.Data.APIRequest;
import andre.pt.projectoeseminario.Preferences;
import andre.pt.projectoeseminario.R;
import andre.pt.projectoeseminario.Services.CopyMenuListener;

public class PreferencesActivity extends ParentActivity{

    private static final String TAG = "Portugal:Preferences";
    private static final int NOTIFICATION_SUPER_SECRET_ID = 1;

    private boolean service_state;
    private int user;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void init() {
        setContentView(R.layout.activity_preferences);

        //Setup preferences
        user = getIntPreference(Preferences.USER_TOKEN);
        service_state = getBooleanPreference(Preferences.SERVICERUNNING);

        String firebaseID = FirebaseInstanceId.getInstance().getToken();
        String tmp = getStringPreference(Preferences.FIREBASEID);

        //On this case its our first launch
        if(tmp == null) {
            service_state = true;
            saveBooleanPreference(Preferences.SERVICERUNNING, true);
        }

        //Upload of the firebaseID, when it changed, or when we know that it isnt registered
        if(tmp == null || !tmp.equals(firebaseID))
            handleNewFirebaseID(user, firebaseID);

        //Launch notification so we can invoke the clipboard chooser
        if(service_state)
            launchNotification();

        Preference[] preferences = new Preference[]{new Preference(getString(R.string.Service_Running_Option_Title), getString(R.string.Service_Running_Option_Description), service_state)};
        HashMap<String, CompoundButton.OnCheckedChangeListener> preferencesActions = new HashMap<>();
        preferencesActions.put(getString(R.string.Service_Running_Option_Title), (buttonView, isChecked) -> {
            if(!isChecked){
                stopService();
            }else{
                startService();
            }

            //Why are we saving this here?Because on Pause/OnDestroy is not reliable.
            saveBooleanPreference(Preferences.SERVICERUNNING, isChecked);
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        PreferencesAdapter adapter = new PreferencesAdapter(preferences, preferencesActions);

        recyclerView.setAdapter(adapter);

        toolbar.setTitle(getString(R.string.app));

    }

    //Creates a non-dismissable notification so that we can launch the clipboard chooser
    private void launchNotification() {
        //Intent it =
        //Major requirement - Without this flags
        //it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);

        Notification.Builder notification = new Notification.Builder(this)
            .setContentTitle(getString(R.string.app_name))
            .setSmallIcon(android.R.color.transparent)
            .setContentText(getString(R.string.Notification_Description))
            .setOngoing(true)
            .setContentIntent(PendingIntent.getActivity(getApplicationContext(), NOTIFICATION_SUPER_SECRET_ID, new Intent(getApplicationContext(), ClipboardContentChooser.class), PendingIntent.FLAG_UPDATE_CURRENT));


        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(NOTIFICATION_SUPER_SECRET_ID, notification.build());
        //finish();
    }

    /*
    *   Uploads the device firebase ID if we detect that it may not be present in the server
    */
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

    /*
    *   Checks if the clipboard listener service is running
    */
    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if(CopyMenuListener.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /*
    *   Starts the clipboard listener service
    */
    private void startService(){
        if(!isServiceRunning()){
            startService(new Intent(this, CopyMenuListener.class));
        }
    }

    /*
    *   Stops the clipboard listener service
    */
    private void stopService(){
        stopService(new Intent(this, CopyMenuListener.class));


    }

}
