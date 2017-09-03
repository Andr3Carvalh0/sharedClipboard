package andre.pt.projectoeseminario.View.Fragments;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import java.util.HashMap;

import andre.pt.projectoeseminario.View.Adapters.Entities.Preference;
import andre.pt.projectoeseminario.View.Adapters.Settings.PreferencesAdapter;
import andre.pt.projectoeseminario.View.Activities.Interfaces.SettingsActions;
import andre.pt.projectoeseminario.View.Fragments.Interfaces.ParentFragment;
import andre.pt.projectoeseminario.Controller.Preferences;
import andre.pt.projectoeseminario.R;

/**
 * Fragment that is shown in the SettingsActivity.
 */
public class PreferencesFragment extends ParentFragment {
    private static final String TAG = "Portugal:PreferencesFR";
    private View mView;
    private RecyclerView mRecyclerView;
    private SettingsActions activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_preferences, container, false);
        activity = ((SettingsActions)getActivity());
        buildPreferences();
        return mView;
    }

    /**
     * Adds all components to the view, and setups the events listeners.
     */
    private void buildPreferences() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        Preference[] preferences = new Preference[]{
                new Preference(getString(R.string.Service_Running_Option_Title), getString(R.string.Service_Running_Option_Description), activity.getServiceState()),
                new Preference(getString(R.string.History_Settings), getString(R.string.History_Description_Settings), activity.getNotificationState())
        };

        HashMap<String, CompoundButton.OnCheckedChangeListener> preferencesActions = new HashMap<>();

        preferencesActions.put(getString(R.string.Service_Running_Option_Title), (buttonView, isChecked) -> {
            if(!isChecked){
                activity.stopService();
            }else{
                activity.startService();
            }

            //Why are we saving this here?Because on Pause/OnDestroy is not reliable.
            activity.savePreference(Preferences.SERVICERUNNING, isChecked);
        });

        preferencesActions.put(getString(R.string.History_Settings), (buttonView, isChecked) -> {
            if(!isChecked){
                activity.cancelNotification();
            }else{
                activity.launchNotification();
            }

            //Why are we saving this here?Because on Pause/OnDestroy is not reliable.
            activity.savePreference(Preferences.NOTIFICATION_STATE, isChecked);
        });


        PreferencesAdapter adapter = new PreferencesAdapter(preferences, preferencesActions);
        mRecyclerView.setAdapter(adapter);
    }
}
