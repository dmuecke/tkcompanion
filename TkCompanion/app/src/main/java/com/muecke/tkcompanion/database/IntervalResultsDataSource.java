package com.muecke.tkcompanion.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.muecke.tkcompanion.model.Result;
import com.muecke.tkcompanion.model.Swimmer;

import java.util.ArrayList;
import java.util.List;

public class IntervalResultsDataSource {
    // Database fields
    private SQLiteDatabase database;
    private DataManager dbHelper;
    private String[] allColumns = { DataManager.COLUMN_NAME, DataManager.COLUMN_TE, DataManager.COLUMN_COMP, DataManager.COLUMN_AVERGAE, DataManager.COLUMN_SPLIT };

    public IntervalResultsDataSource(Context context) {
        dbHelper = new DataManager(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createSplitTime(String name, String sessionId, String competition, List<Integer> splitTimes) {

        try {
            database.delete(DataManager.TABLE_IR, DataManager.COLUMN_NAME + "= ? and " + DataManager.COLUMN_TE + "= ? and " + DataManager.COLUMN_COMP + "= ?",
                    new String[]{name,sessionId,competition});
        } catch (SQLException e) {
            Log.d("createPresence", e.getMessage());
        }

        ContentValues values = new ContentValues();
        values.put(DataManager.COLUMN_NAME, name);
        values.put(DataManager.COLUMN_TE, sessionId);
        values.put(DataManager.COLUMN_COMP, competition);

        StringBuilder stringBuilder = new StringBuilder();
        boolean first = true;
        int total = 0;
        for (Integer splitTime : splitTimes) {
            if (first) {
                first=false;
            } else {
                stringBuilder.append("/");
            }
            stringBuilder.append(Swimmer.formatTime(splitTime));
            total += splitTime;
        }
        int avg = total;
        if (splitTimes.size() > 0) {
            avg = avg / splitTimes.size();
        }
        values.put(DataManager.COLUMN_AVERGAE, avg);
        values.put(DataManager.COLUMN_SPLIT, stringBuilder.toString());

        database.insert(DataManager.TABLE_IR, null, values);

    }


    public List<Result> getFilteredSplits(String session) {
        List<Result> splits = new ArrayList<Result>();

        Cursor cursor = database.query(DataManager.TABLE_IR,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if ("All".equalsIgnoreCase(session) || session.equalsIgnoreCase(cursor.getString(1))) {
                Result st = cursorToString(cursor);
                splits.add(st);
            }
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return splits;
    }

    private Result cursorToString(Cursor c) {
        return new Result(c.getString(0), c.getInt(3),c.getString(1), c.getString(2), 0);
    }

    public void deleteFilteredSplits(String session) {
        if ("All".equalsIgnoreCase(session)) {
            database.delete(DataManager.TABLE_IR,null,null);
        } else {
            database.delete(DataManager.TABLE_IR, DataManager.COLUMN_TE + "= ?", new String[]{session});
        }

    }
}