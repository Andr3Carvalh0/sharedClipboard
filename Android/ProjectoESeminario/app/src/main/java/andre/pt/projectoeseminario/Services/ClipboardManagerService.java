package andre.pt.projectoeseminario.Services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import andre.pt.projectoeseminario.Data.APIRequest;

public class ClipboardManagerService extends IntentService{
    private final String TAG = "Portugal:Download";

    public static final String ACTION_PASTE = "andre.pt.projectoeseminario.PASTE_ACTION";
    public static final String ACTION_COPY = "andre.pt.projectoeseminario.COPY_ACTION";
    private APIRequest mApi;

    public ClipboardManagerService() {
        super("ClipboardManagerService");
        mApi = new APIRequest(null, getBaseContext());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            mApi = new APIRequest(null, this);

            if (ACTION_PASTE.equals(action)) {
                handlePaste(intent.getIntExtra("token", 0));
            } else if (ACTION_COPY.equals(action)) {
                handleCopy(intent.getIntExtra("token", 0), intent.getStringExtra("text"));
            }
        }
    }

    private void handlePaste(int token) {
        Log.v("Projecto", "ClipboardManagerService:handlePaste");
        //mApi.fetchInformation(token);

    }

    private void handleCopy(int token, String text) {
        //We cannot update when the user token isnt valid or when we cannot get the copied text
        if(token == 0 || text == null)
            return;

        Log.v(TAG, "Uploading text to server");
        mApi.pushTextInformation(token, text);

    }
}
