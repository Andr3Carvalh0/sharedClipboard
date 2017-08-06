package andre.pt.projectoeseminario.Activities;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;

import andre.pt.projectoeseminario.Activities.Interfaces.History;
import andre.pt.projectoeseminario.Adapters.Fragments.TabViewPager;
import andre.pt.projectoeseminario.API.APIRequest;
import andre.pt.projectoeseminario.Activities.Interfaces.SettingsActions;
import andre.pt.projectoeseminario.Preferences;
import andre.pt.projectoeseminario.R;
import andre.pt.projectoeseminario.Services.CopyMenuListener;

public class SettingsActivity extends History implements TabLayout.OnTabSelectedListener, SettingsActions{

    private static final String TAG = "Portugal:Preferences";
    private static final int NOTIFICATION_SUPER_SECRET_ID = 1;

    private boolean service_state;
    private int user;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ViewPager mViewPager;
    private TabViewPager adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init() {
        setContentView(R.layout.activity_preferences);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app));
        setSupportActionBar(toolbar);

        //Setup preferences
        user = getIntPreference(Preferences.USER_TOKEN);
        service_state = getBooleanPreference(Preferences.SERVICERUNNING);
        boolean hasShownSetup = getBooleanPreference(Preferences.SETUPSHOWN);

        String firebaseID = FirebaseInstanceId.getInstance().getToken();
        String tmp = getStringPreference(Preferences.FIREBASEID);

        //On this case its our first launch
        if(!hasShownSetup) {
            showTips();
            //saveBooleanPreference(Preferences.SETUPSHOWN, true);

            service_state = true;
            saveBooleanPreference(Preferences.SERVICERUNNING, true);
        }

        //Upload of the firebaseID, when it changed, or when we know that it isnt registered
        if(tmp == null || !tmp.equals(firebaseID))
            handleNewFirebaseID(user, firebaseID);

        //Launch notification so we can invoke the clipboard chooser
        if(service_state && getBooleanPreference(Preferences.NOTIFICATION_STATE))
            launchNotification();

        initViewPager();
    }

    //Show tips for first time users
    private void showTips() {
        Point size = new Point();
        this.getWindowManager().getDefaultDisplay().getSize(size);
        int width = size.x;
        int height = size.y;

        TapTargetView.showFor(this, TapTarget.forBounds(new Rect((int) (width * 0.87), (int) (height * 0.93), (int) (width * 0.92), (int) (height * 0.95)), "Hi", "This is your tape")
                .transparentTarget(true)
                .targetCircleColor(R.color.white)
                .textColor(R.color.white));
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


    /*
    * Everything related with the initialization of the tablayout
    * */
    private void initViewPager() {
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabLayout);

        mTabLayout.addTab(mTabLayout.newTab().setIcon(getResources().getDrawable(R.drawable.ic_restore_white_24dp, null)).setText(getString(R.string.History)));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(getResources().getDrawable(R.drawable.ic_settings_white_24dp, null)).setText(getString(R.string.Settings)));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mViewPager = (ViewPager) findViewById(R.id.pager);

        adapter = new TabViewPager(getSupportFragmentManager(), mTabLayout.getTabCount(), this);

        mViewPager.setAdapter(adapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(this);

    }

    //Cancels the notification
    public void cancelNotification() {
        ((NotificationManager)getSystemService(NOTIFICATION_SERVICE)).cancel(NOTIFICATION_SUPER_SECRET_ID);
    }

    //Creates a non-dismissable notification so that we can launch the clipboard chooser
    public void launchNotification() {
        Intent it = new Intent(getApplicationContext(), ClipboardContentChooser.class);
        it.setFlags(it.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);

        Notification.Builder
                notification = new Notification.Builder(this)
                .setContentTitle(getString(R.string.app))
                .setSmallIcon(R.drawable.ic_assignment_red_400_24dp)
                .setContentText(getString(R.string.History_Description))
                .setOngoing(true)
                .setContentIntent(PendingIntent.getActivity(getApplicationContext(), NOTIFICATION_SUPER_SECRET_ID, it, PendingIntent.FLAG_UPDATE_CURRENT));

        notification.setColor(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M
                                ? getColor(R.color.primary)
                                : getResources().getColor(R.color.primary));

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(NOTIFICATION_SUPER_SECRET_ID, notification.build());
    }

    @Override
    protected void setupEvents() {
        if(service_state)
            startService();
        else
            stopService();

        Log.d(TAG, "is Service Running: " + isServiceRunning());
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
    public void startService(){
        if(!isServiceRunning()){
            startService(new Intent(this, CopyMenuListener.class));
        }
    }

    /*
    *   Stops the clipboard listener service
    */
    public void stopService(){
        stopService(new Intent(this, CopyMenuListener.class));
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public boolean getServiceState() {
        return service_state;
    }

    public void savePreference(String key, boolean isChecked) {
        saveBooleanPreference(key, isChecked);
    }

    public boolean getNotificationState() {
        return getBooleanPreference(Preferences.NOTIFICATION_STATE);
    }

    @Override
    public boolean getMobileDataState() {
        return getBooleanPreference(Preferences.USEMOBILEDATA);
    }

    @Override
    public List<String> getCategoryElements(String category) {
        return null;
    }

    @Override
    public void onBackPressed() {
        if (adapter.isInDetailedView(mViewPager.getCurrentItem())){
            adapter.returnToCategories();
            return;
        }

        super.onBackPressed();
    }
}
