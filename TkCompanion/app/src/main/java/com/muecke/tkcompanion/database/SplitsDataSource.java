package com.muecke.tkcompanion.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;

import com.muecke.tkcompanion.model.Person;
import com.muecke.tkcompanion.model.Swimmer;

import java.util.ArrayList;
import java.util.List;

public class SplitsDataSource {
    // Database fields
    private SQLiteDatabase database;
    private DataManager dbHelper;
    private String[] allColumns = { DataManager.COLUMN_NAME, DataManager.COLUMN_TE, DataManager.COLUMN_COMP, DataManager.COLUMN_TOTAL, DataManager.COLUMN_SPLIT };

    public SplitsDataSource(Context context) {
        dbHelper = new DataManager(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createSplitTime(String name, String sessionId, String competition,
                                int total, List<Integer> splitTimes) {

        try {
            database.delete(DataManager.TABLE_SP, DataManager.COLUMN_NAME + "= ? and " + DataManager.COLUMN_TE + "= ? and " + DataManager.COLUMN_COMP + "= ?",
                    new String[]{name,sessionId,competition});
        } catch (SQLException e) {
            Log.d("createPresence", e.getMessage());
        }

        ContentValues values = new ContentValues();
        values.put(DataManager.COLUMN_NAME, name);
        values.put(DataManager.COLUMN_TE, sessionId);
        values.put(DataManager.COLUMN_COMP, competition);
        values.put(DataManager.COLUMN_TOTAL, total);

        StringBuilder stringBuilder = new StringBuilder();
        boolean first = true;
        for (Integer splitTime : splitTimes) {
            if (first) {
                first=false;
            } else {
                stringBuilder.append("/");
            }
            stringBuilder.append(Swimmer.formatTime(splitTime));
        }
        values.put(DataManager.COLUMN_SPLIT, stringBuilder.toString());

        database.insert(DataManager.TABLE_SP, null, values);

    }


    public List<String> getFilteredSplits(String session) {
        List<String> splits = new ArrayList<String>();

        Cursor cursor = database.query(DataManager.TABLE_SP,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if ("All".equalsIgnoreCase(session) || session.equalsIgnoreCase(cursor.getString(1))) {
                String st = cursorToString(cursor);
                splits.add(st);
            }
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return splits;
    }

    private String cursorToString(Cursor cursor) {
        StringBuilder builder = new StringBuilder();
        builder.append(cursor.getString(1)).append(":").append(cursor.getString(0)).append("  ");
        builder.append(cursor.getString(2)).append("  ").append(Swimmer.formatTime(cursor.getInt(3))).append(" |").append(cursor.getString(4));
        return builder.toString();
    }

    public void deleteFilteredSplits(String session) {
        if ("All".equalsIgnoreCase(session)) {
            database.delete(DataManager.TABLE_SP,null,null);
        } else {
            database.delete(DataManager.TABLE_SP, DataManager.COLUMN_TE + "= ?", new String[]{session});
        }

    }
}
