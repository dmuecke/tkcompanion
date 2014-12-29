package com.muecke.tkcompanion.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PresenceDataSource {
    // Database fields
    private SQLiteDatabase database;
    private DataManager dbHelper;
    private String[] allColumns = { DataManager.COLUMN_NAME, DataManager.COLUMN_TE };

    public PresenceDataSource(Context context) {
        dbHelper = new DataManager(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createPresence(String name, String sessionId, int pool) {

        try {
            database.delete(DataManager.TABLE_PR, DataManager.COLUMN_NAME + "= ? and " + DataManager.COLUMN_TE + "= ?", new String[]{name,sessionId});
        } catch (SQLException e) {
            Log.d("createPresence", e.getMessage());
        }

        ContentValues values = new ContentValues();
        values.put(DataManager.COLUMN_NAME, name);
        values.put(DataManager.COLUMN_TE, sessionId);
        values.put(DataManager.COLUMN_PL, pool);

        database.insert(DataManager.TABLE_PR, null, values);

    }

    public List<String> getAllAvailablePersons(String sessionId) {
        List<String> persons = new ArrayList<String>();

        Cursor cursor = database.query(DataManager.TABLE_PR,
                allColumns, "session= ?", new String[] {sessionId}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            persons.add(cursor.getString(0));
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        return persons;
    }
}
