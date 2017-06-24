package andre.pt.projectoeseminario.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.FileNotFoundException;

public class ClipboardContentProvider extends ContentProvider {
    //Where is stored the data that the server contains.Used also to support images in the clipboard
    private static final String RECENT_TABLE = "recent";

    private String[] tablesToCreate = new String[]{RECENT_TABLE, HISTORY_TEXT_TABLE, HISTORY_CONTACTS_TABLE, HISTORY_LINKS_TABLE};

    //Where we store all the history of the clipboard.
    //Initially the implementation would have been: "do it in volatile memory", but since we have a content provider
    //Why the hell not?
    private static final String HISTORY_TEXT_TABLE = "history_text";
    private static final String HISTORY_LINKS_TABLE = "history_links";
    private static final String HISTORY_IMAGES_TABLE = "history_images";
    private static final String HISTORY_CONTACTS_TABLE = "history_contacts";

    private static final String AUTHORITY = "andre.pt.projectoeseminario";

    //URIS to our tables
    public static final Uri RECENT_URI = Uri.parse("content://" + AUTHORITY + "/" + RECENT_TABLE);

    public static final Uri HISTORY_TEXT_URI = Uri.parse("content://" + AUTHORITY + "/" + HISTORY_TEXT_TABLE);
    public static final Uri HISTORY_LINKS_URI = Uri.parse("content://" + AUTHORITY + "/" + HISTORY_LINKS_TABLE);
    public static final Uri HISTORY_IMAGES_URI = Uri.parse("content://" + AUTHORITY + "/" + HISTORY_IMAGES_TABLE);
    public static final Uri HISTORY_CONTACTS_URI = Uri.parse("content://" + AUTHORITY + "/" + HISTORY_CONTACTS_TABLE);

    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final int RECENT = 1;
    public static final int HISTORY = 2;
    public static final int HISTORY_ELEMENT = 3;

    static {
        matcher.addURI(AUTHORITY, RECENT_TABLE, RECENT);

        //Every table related to history
        matcher.addURI(AUTHORITY, HISTORY_TEXT_TABLE, HISTORY);
        matcher.addURI(AUTHORITY, HISTORY_TEXT_TABLE + "/#", HISTORY_ELEMENT);

        matcher.addURI(AUTHORITY, HISTORY_IMAGES_TABLE, HISTORY);
        matcher.addURI(AUTHORITY, HISTORY_IMAGES_TABLE + "/#", HISTORY_ELEMENT);

        matcher.addURI(AUTHORITY, HISTORY_LINKS_TABLE, HISTORY);
        matcher.addURI(AUTHORITY, HISTORY_LINKS_TABLE + "/#", HISTORY_ELEMENT);

        matcher.addURI(AUTHORITY, HISTORY_CONTACTS_TABLE, HISTORY);
        matcher.addURI(AUTHORITY, HISTORY_CONTACTS_TABLE + "/#", HISTORY_ELEMENT);
    }

    @Override
    public boolean onCreate() {
        Database db = new Database(getContext(), tablesToCreate);
        return false;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public ParcelFileDescriptor openFile(@NonNull Uri uri, @NonNull String mode) throws FileNotFoundException {
        return super.openFile(uri, mode);
    }
}
