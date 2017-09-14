package andre.pt.projectoeseminario.Controller.Data.Cache;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import andre.pt.projectoeseminario.Controller.Classifiers.Classifiers;
import andre.pt.projectoeseminario.Controller.Data.ContentProvider.ResourcesContentProviderContent;

/**
 * Manages the content from the contentProvider/cache
 */
public class Cache implements ICache {
    private HashMap<String, LinkedList<String>> cache;
    private Context ctx;
    private ContentResolver cresolver;
    private static final HashMap<String, Uri> router;

    static {
        router = new HashMap<>();
        router.put(ResourcesContentProviderContent.Text.TABLE_NAME, ResourcesContentProviderContent.Text.CONTENT_URI);
        router.put(ResourcesContentProviderContent.Links.TABLE_NAME, ResourcesContentProviderContent.Links.CONTENT_URI);
        router.put(ResourcesContentProviderContent.Contacts.TABLE_NAME, ResourcesContentProviderContent.Contacts.CONTENT_URI);
        router.put(ResourcesContentProviderContent.Recent.TABLE_NAME, ResourcesContentProviderContent.Recent.CONTENT_URI);

    }

    public Cache(Context ctx, ContentResolver cresolver){
        this.ctx = ctx;
        this.cache = new HashMap<>();
        this.cresolver = cresolver;
    }

    /**
     * Adds the value to the cache/content provider.
     * @param value the value to store
     * @param category the category of the value
     */
    @Override
    public void store(String value, String category) {
        ContentValues values = new ContentValues();
        values.put("content", value);

        final Uri table = router.get(category);
        cresolver.insert(table, values);

        cache.computeIfAbsent(category, (s) -> new LinkedList<String>());
        cache.get(category).add(value);
    }

    /**
     * Adds values from contentProvider to the cache
     * @param values the values from the content provider
     * @param table the key
     */
    private void addToCache(String[] values, String table){
        cache.computeIfAbsent(table, (s) -> new LinkedList<String>());
        cache.get(table).addAll(Arrays.stream(values).collect(Collectors.toList()));
    }

    /**
     * Adds the value to the recents table
     * @param value the value to store
     */
    private void addToRecents(String value){
        ContentValues values = new ContentValues();
        values.put("content", value);

        cresolver.delete(ResourcesContentProviderContent.Recent.CONTENT_URI, null, null);
        cresolver.insert(ResourcesContentProviderContent.Recent.CONTENT_URI, values);
    }

    /**
     * Adds the value to the cache/content provider.It cassifies the value to know which table to insert
     * the value into
     * @param value the value to store
     */
    @Override
    public void store(String value) {
        String table = ResourcesContentProviderContent.Text.TABLE_NAME;

        if(Classifiers.isContact(value))
            table = ResourcesContentProviderContent.Contacts.TABLE_NAME;

        if(Classifiers.isLink(value))
            table = ResourcesContentProviderContent.Links.TABLE_NAME;

        store(value, table);
        addToRecents(value);
    }

    /**
     * Returns every value from the category @category
     * @param category
     * @return every value in the category
     */
    @Override
    public String[] pull(String category) {
        final LinkedList<String> content = cache.get(category);

        if(content == null){
            final String[] tableContents = getTableContents(category);
            addToCache(tableContents, category);
            return tableContents;
        }

        return content.toArray(new String[content.size()]);
    }


    /**
     * Reads every value from the table @table
     * @param table the table
     */
    private String[] getTableContents(String table) {
        List<String> tmp = new LinkedList<>();
        Cursor cursor = null;

        try {
            cursor = cresolver.query(router.get(table), null, null, null, null);

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                tmp.add(cursor.getString(1));

                cursor.moveToNext();
            }
        }catch (NullPointerException e){

        }finally {
            if(cursor != null)
                cursor.close();
        }

        return tmp.stream().toArray(String[]::new);
    }
}
