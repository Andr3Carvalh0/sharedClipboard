package andre.pt.projectoeseminario.Services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import andre.pt.projectoeseminario.Activities.ParentActivity;
import andre.pt.projectoeseminario.Data.APIRequest;
import andre.pt.projectoeseminario.Data.Interface.Responses.IResponse;



public class DownloadService extends IntentService implements IResponse {
    public static final String ACTION_PASTE = "andre.pt.projectoeseminario.PASTE_ACTION";
    public static final String ACTION_COPY = "andre.pt.projectoeseminario.COPY_ACTION";
    private APIRequest mApi;

    public DownloadService() {
        super("DownloadService");
        mApi = new APIRequest((IResponse) this, getBaseContext());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PASTE.equals(action)) {
                handlePaste(intent.getIntExtra("token", 0));
            } else if (ACTION_COPY.equals(action)) {
                handleCopy();
            }
        }
    }

    private void handlePaste(int token) {
        Log.v("Projecto", "DownloadService:handlePaste");
        mApi.fetchInformation(token);




    }

    private void handleCopy() {
        Log.v("Projecto", "DownloadService:handleCopy");
    }



    @Override
    public void handlePull(String msg) {
        Log.v("Andre", msg);

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
