package andre.pt.projectoeseminario;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Handles everything related to user preferences, and the SharedPreferences Objects
*/
public class Preferences {
    public static String USER_TOKEN = "user_token";
    public static String AUTHENTICATED = "authenticated";
    public static String SERVICERUNNING = "run_service";
    public static String FIREBASEID = "firebaseID";
    public static String NOTIFICATION_STATE = "show_notification";
    public static String SETUPSHOWN = "afterBinding";
    private SharedPreferences.Editor editor;
    private SharedPreferences shared;

    public Preferences(Context ctx){
        shared = PreferenceManager.getDefaultSharedPreferences(ctx);
        editor = shared.edit();
    }

    public void saveIntPreference(String key, int value){
        editor.putInt(key, value);
        editor.apply();
    }

    public void saveBooleanPreference(String key, boolean value){
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void saveStringPreference(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public int getIntPreference(String key){
        return shared.getInt(key, 0);
    }

    public boolean getBooleanPreference(String key){
        return shared.getBoolean(key, false);
    }

    public String getStringPreference(String key) {
        return shared.getString(key, null);
    }

    public void clearAll(){
        editor.clear();
        editor.commit();
    }

}
