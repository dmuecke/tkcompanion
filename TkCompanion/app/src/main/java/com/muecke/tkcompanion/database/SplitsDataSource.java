package com.muecke.tkcompanion.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SplitsDataSource {
    // Database fields
    private SQLiteDatabase database;
    private DataManager dbHelper;
    private String[] allColumns = { DataManager.COLUMN_NAME, DataManager.COLUMN_TE, DataManager.COLUMN_COMP, DataManager.COLUMN_SPLIT, DataManager.COLUMN_TOTAL };

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
            stringBuilder.append(splitTime);
        }
        values.put(DataManager.COLUMN_SPLIT, stringBuilder.toString());

        database.insert(DataManager.TABLE_SP, null, values);

    }

}
