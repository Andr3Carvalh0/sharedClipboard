package andre.pt.projectoeseminario.Services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import andre.pt.projectoeseminario.Activities.ParentActivity;
import andre.pt.projectoeseminario.Data.APIRequest;
import andre.pt.projectoeseminario.Data.Interface.Responses.IResponse;



public class DownloadService extends IntentService implements IResponse {
    private final String TAG = "Portugal:Download";

    public static final String ACTION_PASTE = "andre.pt.projectoeseminario.PASTE_ACTION";
    public static final String ACTION_COPY = "andre.pt.projectoeseminario.COPY_ACTION";
    private APIRequest mApi;

    public DownloadService() {
        super("DownloadService");
        mApi = new APIRequest(this, getBaseContext());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

            if (ACTION_PASTE.equals(action)) {
                handlePaste(intent.getIntExtra("token", 0));
            } else if (ACTION_COPY.equals(action)) {
                handleCopy(intent.getIntExtra("token", 0), intent.getStringExtra("text"));
            }
        }
    }

    private void handlePaste(int token) {
        Log.v("Projecto", "DownloadService:handlePaste");
        //mApi.fetchInformation(token);




    }

    private void handleCopy(int token, String text) {
        //We cannot update when the user token isnt valid or when we cannot get the copied text
        if(token == 0 || text == null)
            return;



        Log.v(TAG, "FirebaseID = " + FirebaseInstanceId.getInstance().getToken());
    }

    @Override
    public void handlePull(String msg) {
        Log.v(TAG, msg);

    }

    @Override
    public void handlePush() {

    }







    @Override
    public void handleSuccessfullyLogin(int user) {

    }

    @Override
    public void handleNonExistingAccount() {

    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void hideProgressDialog() {

    }

    @Override
    public void handleError(String title, String message) {

    }

}
