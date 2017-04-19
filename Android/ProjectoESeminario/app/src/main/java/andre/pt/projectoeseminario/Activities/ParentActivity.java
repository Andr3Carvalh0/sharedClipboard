package andre.pt.projectoeseminario.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import andre.pt.projectoeseminario.R;

public abstract class ParentActivity extends AppCompatActivity {

    private SharedPreferences.Editor editor;
    private SharedPreferences shared;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shared = getSharedPreferences(getString(R.string.sharedPreferencesKey), Context.MODE_PRIVATE);
        editor = shared.edit();

        init();
        setupEvents();

    }

    protected abstract void init();
    protected abstract void setupEvents();

    protected boolean hasCompletedSetup(){
        return getBooleanPreference("authenticated");
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
        editor.putInt(key, value);
        editor.apply();
    }

    protected void saveBooleanPreference(String key, boolean value){
        editor.putBoolean(key, value);
        editor.apply();
    }

    protected int getIntPreference(String key){
        return shared.getInt(key, 0);
    }

    protected boolean getBooleanPreference(String key){
        return shared.getBoolean(key, false);
    }

}
