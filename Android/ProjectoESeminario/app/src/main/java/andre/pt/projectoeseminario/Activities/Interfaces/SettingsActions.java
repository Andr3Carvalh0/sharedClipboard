package andre.pt.projectoeseminario.Activities.Interfaces;

public interface SettingsActions {
    void cancelNotification();
    void launchNotification();

    boolean getServiceState();
    boolean getNotificationState();


    void stopService();
    void startService();

    void savePreference(String servicerunning, boolean isChecked);
}
