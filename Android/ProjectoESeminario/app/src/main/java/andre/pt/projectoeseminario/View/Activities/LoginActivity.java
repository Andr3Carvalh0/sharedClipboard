package andre.pt.projectoeseminario.View.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import andre.pt.projectoeseminario.Controller.API.APIRequest;
import andre.pt.projectoeseminario.Controller.API.Interface.Responses.IAuthenticate;
import andre.pt.projectoeseminario.Controller.Preferences;
import andre.pt.projectoeseminario.R;
import andre.pt.projectoeseminario.View.Activities.Interfaces.ParentActivity;

public class LoginActivity extends ParentActivity implements IAuthenticate, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "Portugal:Login";
    private static final int SUPER_SECRET_ID = 27;
    private static final String token = "XXX.apps.googleusercontent.com";
    private GoogleApiClient mGoogleApiClient;
    private SignInButton signInButton;
    private APIRequest mApi;
    private String id;

    /**
     * Used to bind every ui object with its object
     */
    protected void binding() {
        setContentView(R.layout.activity_login);
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);


    }

    /**
     * Used to afterBinding every onclickevent, onfocuschange, etc...
     */
    protected void afterBinding() {
        mApi = new APIRequest(this, this);
        signInButton.setOnClickListener(v -> signIn());
        initAuthorizeGoogleAccess();
    }

    /**
     * Begins the signIn Process
     */
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, SUPER_SECRET_ID);
    }

    /**
     * Inits the Google API objects
     */
    private void initAuthorizeGoogleAccess() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(token)
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    /**
     * Shows a dialog asking the user if it wants to create an account with the info that he provided earlier.
     * We do this because the server "told us", that an account with the email didn't existed.
    */
    @Override
    public void handleNonExistingAccount() {
        try {
            showDialogWithPositiveAndNegativeButtons(getString(R.string.CreateAccount_Title), getString(R.string.CreateAccout_Message),
                (dialog, which) -> {
                    dialog.dismiss();
                    mApi.handleAccountCreation(id);
                },
                (dialog, which) -> dialog.dismiss()
            );
        }catch (Exception e){
            Log.v(TAG, "Cannot communicate with server right now!");
            showDialogWithPositiveButton(getString(R.string.GenericError_Title), getString(R.string.No_Wifi), getString(R.string.PositiveButton_Title), (dialog, which) -> dialog.dismiss());
        }
    }

    /**
     * Handles a successful login.
     * This method then will save the user token in the SharedPreferences
     * And then will start the Settings Activity.
     * To save work on the Settings Activity we pass the user token as an intent extra
     * @param sub : the token that represents the user
    */
    @Override
    public void handleSuccessfullyLogin(String sub, long order) {
        saveStringPreference(Preferences.USER_TOKEN, sub);
        saveBooleanPreference(Preferences.AUTHENTICATED, true);
        saveLongPreference(Preferences.ORDER, order);

        Intent it = new Intent(this, SettingsActivity.class);
        it.putExtra("token", sub);
        it.putExtra("order", order);
        startActivity(it);
        finish();
    }

    /**
     * Used to show a dialog alerting the user that the service isn't operational.This Dialog only has one button("ok"),
     * that its function is to dismiss the dialog.
     * @param title : The Dialog Title
     * @param message : The Dialog body
    */
    @Override
    public void handleError(String title, String message) {
        showDialogWithPositiveButton(title, message, getString(R.string.PositiveButton_Title), (dialog, which) -> dialog.dismiss());
    }

    /**
     * Shows a dialog with a infinity progress bar, to indicate to the user that we are doing something...
     */
    @Override
    public void showProgressDialog() {
        showProgressDialog("", getString(R.string.Loading_Message), false);
    }

    /**
     * Hides the progress dialog, that we created early.
     */
    @Override
    public void hideProgressDialog() {
        super.hideProgressDialog();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == SUPER_SECRET_ID) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    /**
     * Called when the used logs in
     * @param result the information from the google server
     */
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            try {
                Log.v(TAG, "Google sing-in successful");
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();

                id = acct.getIdToken();

                mApi.handleAuthentication(id);
            }catch (NullPointerException e){
                Log.v(TAG, "Google sing-in error");
                new AlertDialog.Builder(this).setMessage(getString(R.string.Google_error)).create().show();
            }
        } else {
            Log.v(TAG, "Google sing-in error");
            new AlertDialog.Builder(this).setMessage(getString(R.string.Google_error)).create().show();
        }
    }

    //Not used
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
