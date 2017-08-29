package andre.pt.projectoeseminario.Activities.Interfaces;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;

import andre.pt.projectoeseminario.ContentProvider.ResourcesContentProviderContent;
import andre.pt.projectoeseminario.Preferences;
import andre.pt.projectoeseminario.Projecto;
import andre.pt.projectoeseminario.R;

public abstract class ParentActivity extends AppCompatActivity {
    private Preferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreferences = new Preferences(this);

        binding();
        afterBinding();

    }

    protected abstract void binding();
    protected abstract void afterBinding();

    protected boolean hasCompletedSetup(){
        return mPreferences.getBooleanPreference(Preferences.AUTHENTICATED);
    }

    protected ProgressDialog waitingDialog;

    /*
    *   Shows a progress dialog
    */
    protected void showProgressDialog(String title, String message, boolean cancelable){
        waitingDialog = ProgressDialog.show(this, title, message, true, cancelable);
    }

    /*
    *   Hides the progress dialog
    */
    protected void hideProgressDialog(){
        if(waitingDialog != null){
            waitingDialog.dismiss();
        }
    }

    /*
    *   Build a dialog with only one button.
    *   Usually used to display server errors.
    *
    *   @param title : the dialog title
    *   @param message : the dialog body message
    *   @param buttonTitle : the button text
    *   @param onClickLister : action to do when the button is clicked
    *
    */
    protected void showDialogWithPositiveButton(String title, String message, String buttonTitle, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(buttonTitle, onClickListener)
                .show();
    }

    /*
    *   Build a dialog with two buttons.
    *   Usually used to ask the user for actions.
    *
    *   @param title : the dialog title
    *   @param message : the dialog body message
    *   @param negativeListener : action to do when the negative button(No, Nop, [insert here a negative message]) is clicked
    *   @param positiveListener : action to do when the positive button(Yes, Sure!, [insert here a positive message]) is clicked
    *
    */
    protected void showDialogWithPositiveAndNegativeButtons(String title, String message, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.PositiveButton_Default_Title, positiveListener)
                .setNegativeButton(R.string.NegativeButton_Default_Title, negativeListener)
                .show();
    }


    protected void saveIntPreference(String key, int value){
        mPreferences.saveIntPreference(key, value);
    }

    protected void saveBooleanPreference(String key, boolean value){
        mPreferences.saveBooleanPreference(key, value);
    }

    protected void saveStringPreference(String key, String value){
        mPreferences.saveStringPreference(key, value);
    }

    protected int getIntPreference(String key){
       return mPreferences.getIntPreference(key);
    }

    protected boolean getBooleanPreference(String key){
       return mPreferences.getBooleanPreference(key);
    }

    protected String getStringPreference(String key){
        return mPreferences.getStringPreference(key);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Projecto)getApplication()).setVisible();
    }

    @Override
    public void onPause() {
        super.onPause();
        ((Projecto)getApplication()).setInvisible();
    }

}
