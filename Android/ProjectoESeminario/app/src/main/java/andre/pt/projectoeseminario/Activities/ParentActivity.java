package andre.pt.projectoeseminario.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import andre.pt.projectoeseminario.Preferences;
import andre.pt.projectoeseminario.R;

public abstract class ParentActivity extends AppCompatActivity {
    private Preferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreferences = new Preferences(this);

        init();
        setupEvents();

    }

    protected abstract void init();
    protected abstract void setupEvents();

    protected boolean hasCompletedSetup(){
        return mPreferences.getBooleanPreference(Preferences.HASCOMPLETEDSETUP);
    }

    protected ProgressDialog waitingDialog;

    protected void showProgressDialog(String title, String message, boolean cancelable){
        waitingDialog = ProgressDialog.show(this, title, message, true, cancelable);
    }

    protected void hideProgressDialog(){
        if(waitingDialog != null){
            waitingDialog.dismiss();
        }
    }

    protected void showDialogWithPositiveButton(String title, String message, String buttonTitle, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(buttonTitle, onClickListener)
                .show();
    }

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

    protected int getIntPreference(String key){
       return mPreferences.getIntPreference(key);
    }

    protected boolean getBooleanPreference(String key){
       return mPreferences.getBooleanPreference(key);
    }


}
