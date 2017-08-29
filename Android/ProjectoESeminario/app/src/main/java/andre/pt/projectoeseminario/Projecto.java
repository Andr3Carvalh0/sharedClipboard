package andre.pt.projectoeseminario;

import android.app.Application;
import android.content.Intent;

import andre.pt.projectoeseminario.Cache.Cache;
import andre.pt.projectoeseminario.Cache.ICache;

/**
 * Represents the entire application.
 * Contains useful methods to the activities/services
 */
public class Projecto extends Application {
    private boolean visible = true;
    private ICache cache;
    private Preferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        cache = new Cache(getApplicationContext(), getContentResolver());
        preferences = new Preferences(getApplicationContext());
    }

    /**
     * Handles the logout process.
     * If the application is visible, relaunch it.
     */
    public void logOut(){
        preferences.clearAll();
        preferences.saveBooleanPreference(Preferences.SERVICERUNNING, false);

        if(visible) {
            Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }

    /**
     * Changes the application state to visible.
     * Useful when we invoke the logout method
     */
    public void setVisible(){
        visible = true;
    }

    /**
     * Changes the application state to invisible.
     * Useful when we invoke the logout method
     */
    public void setInvisible(){
        visible = false;
    }

    /**
     * Gets all of the previous content of a given category
     * @param category the category where we want the content
     */
    public String[] getContent(String category){
        return cache.pull(category);
    }

    /**
     * Stores the @content for later use.
     * @param content the value of what we last copied.
     */
    public void storeContent(String content){
        cache.store(content);
    }

}
