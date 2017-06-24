package andre.pt.projectoeseminario.ContentProvider;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import andre.pt.projectoeseminario.ContentProvider.DTOS.ClipboardItem;

public class Database extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "clipboardManager.db";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CONTENT = "content";

    private final Context context;
    private String[] tables;

    public Database(Context context, String[] tables) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context.getApplicationContext();
        this.tables = tables;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String table: tables) {
            String CREATE_PRODUCTS_TABLE =
                    "CREATE TABLE " +
                            table + "(" +
                            COLUMN_ID + " INTEGER PRIMARY KEY," +
                            COLUMN_CONTENT + " TEXT" + ")";

            db.execSQL(CREATE_PRODUCTS_TABLE);
        }

    }


    // We are assuming that the database format is final.This is the struct of a table will never change in its live time
    // But if it changes, we dont care about the content, and recreate everything from 0
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String table: tables) {
            db.execSQL("DROP TABLE IF EXISTS " + table);
        }

        onCreate(db);
    }

    public void addItem(ClipboardItem item, String table){
        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTENT, item.getContent());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(table, null, values);
        db.close();
    }

    public boolean deleteItem(ClipboardItem item, String table) {
        boolean result = false;

        String query = "SELECT * FROM " + table + " WHERE " + COLUMN_CONTENT + " =  \"" + item.getContent() + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int id = Integer.parseInt(cursor.getString(0));

            db.delete(table, COLUMN_ID + " = ?", new String[] { id + "" });
            cursor.close();
            result = true;
        }

        db.close();
        return result;
    }
}
