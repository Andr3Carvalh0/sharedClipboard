package andre.pt.projectoeseminario.Services;

import android.app.IntentService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;

import andre.pt.projectoeseminario.Classifiers.Classifiers;
import andre.pt.projectoeseminario.State.ClipboardControllerFactory;
import andre.pt.projectoeseminario.ContentProvider.ResourcesContentProviderContent;
import andre.pt.projectoeseminario.API.APIRequest;
import andre.pt.projectoeseminario.State.ClipboardController;


/*
* Handles what to do when, the firebase service receives an notification
*/
public class ClipboardEventHandler extends IntentService {

    private static final String TAG = "Portugal:ClipHandler";
    private static final HashMap<String,Uri> router;
    public ClipboardEventHandler() {
        this("ClipboardEventHandler");
    }


    static {
        router = new HashMap<>();
        router.put(ResourcesContentProviderContent.Text.TABLE_NAME, ResourcesContentProviderContent.Text.CONTENT_URI);
        router.put(ResourcesContentProviderContent.Links.TABLE_NAME, ResourcesContentProviderContent.Links.CONTENT_URI);
        router.put(ResourcesContentProviderContent.Contacts.TABLE_NAME, ResourcesContentProviderContent.Contacts.CONTENT_URI);
        router.put(ResourcesContentProviderContent.Recent.TABLE_NAME, ResourcesContentProviderContent.Recent.CONTENT_URI);

    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ClipboardEventHandler(String name) {
        super(name);
    }

    /*
    *   Stores the text into the device clipboard
    */
    private void handleTextContent(Context context, String content) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(content, content);
        clipboard.setPrimaryClip(clip);

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final Context context = this.getApplicationContext();

        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData.Item clipboardItem = clipboard.getPrimaryClip().getItemAt(0);
        final ClipboardController clipboardController = ClipboardControllerFactory.getSingleton((clipboardItem.getText() != null ? String.valueOf(clipboardItem.getText()) : "Welcome"));

        assert intent != null;
        final String content = intent.getStringExtra("content");
        final boolean isMIME = intent.getBooleanExtra("isMIME", false);
        final boolean upload = intent.getBooleanExtra("upload", false);
        final int token = intent.getIntExtra("token", 0);
        APIRequest mApi = new APIRequest(null, context);

        try {
            clipboardController.acquireWork();
            if (clipboardController.switchClipboardValue(content, this::addToFilteredTable, this::addToRecentsTable)) {
                Log.v(TAG, "onHandleIntent");

                if (upload) {
                    mApi.pushTextInformation(token, content);

                } else {
                    if (!isMIME) {
                        handleTextContent(context, content);
                        return;
                    }
                }
            }
        } finally {
            clipboardController.releaseWork();
        }

    }


    private String addToFilteredTable(String clipboard_value) {
        String table = ResourcesContentProviderContent.Text.TABLE_NAME;

        if(Classifiers.isContact(clipboard_value))
            table = ResourcesContentProviderContent.Contacts.TABLE_NAME;

        if(Classifiers.isLink(clipboard_value))
            table = ResourcesContentProviderContent.Links.TABLE_NAME;

        ContentValues values = new ContentValues();
        values.put("content", clipboard_value);

        getContentResolver().insert(router.get(table), values);

        return table;
    }

    private boolean addToRecentsTable(String newValue) {
        ContentValues values = new ContentValues();
        values.put("content", newValue);

        getContentResolver().delete(ResourcesContentProviderContent.Recent.CONTENT_URI, null, null);
        getContentResolver().insert(ResourcesContentProviderContent.Recent.CONTENT_URI, values);

        return true;
    }
}
