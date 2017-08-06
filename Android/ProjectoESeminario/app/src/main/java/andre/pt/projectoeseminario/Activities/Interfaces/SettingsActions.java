package andre.pt.projectoeseminario.Activities.Interfaces;

public interface SettingsActions {
    void cancelNotification();
    void launchNotification();

    boolean getServiceState();
    boolean getNotificationState();
    boolean getMobileDataState();

    void stopService();
    void startService();

    void savePreference(String servicerunning, boolean isChecked);
}
