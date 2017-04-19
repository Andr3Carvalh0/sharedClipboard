package andre.pt.projectoeseminario.Activities;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;

import java.util.HashMap;

import andre.pt.projectoeseminario.Adapters.Entities.Preference;
import andre.pt.projectoeseminario.Adapters.PreferencesAdapter;
import andre.pt.projectoeseminario.R;
import andre.pt.projectoeseminario.Services.ClipboardWatcher;

public class PreferencesActivity extends ParentActivity {
    private int userToken;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private PreferencesAdapter adapter;

    private Preference preferences[];
    private HashMap<String, CompoundButton.OnCheckedChangeListener> preferencesActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void init() {
        setContentView(R.layout.activity_preferences);
        userToken = getIntent().getIntExtra("token", 0);

        //Setup preferences
        preferences = new Preference[]{new Preference(getString(R.string.Service_Running_Option_Title), getString(R.string.Service_Running_Option_Description), getBooleanPreference("Service_Running_Option"))};
        preferencesActions = new HashMap<>();
        preferencesActions.put(getString(R.string.Service_Running_Option_Title), new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    System.out.println("OFF");
                    stopService();
                }else{

                    System.out.println("ON");
                    startService();
                }
            }
        });


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PreferencesAdapter(preferences, preferencesActions);

        recyclerView.setAdapter(adapter);

        toolbar.setTitle(getString(R.string.app));



    }

    @Override
    protected void setupEvents() {
        startService();
        setSupportActionBar(toolbar);


    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if(ClipboardWatcher.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void startService(){
        if(!isServiceRunning()){
            startService(new Intent(this, ClipboardWatcher.class));
        }
    }

    private void stopService(){
        stopService(new Intent(this, ClipboardWatcher.class));
    }
}
