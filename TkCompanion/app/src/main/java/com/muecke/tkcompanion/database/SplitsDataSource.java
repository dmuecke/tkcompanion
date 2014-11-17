package com.muecke.tkcompanion.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;

import com.muecke.tkcompanion.model.Person;
import com.muecke.tkcompanion.model.Result;
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
            stringBuilder.append(splitTime);
        }
        values.put(DataManager.COLUMN_SPLIT, stringBuilder.toString());

        database.insert(DataManager.TABLE_SP, null, values);

    }

    public List<Result> getFilteredData(String filterName, String filterArg) {
        if ("by Date".equals(filterName)) {
            return getFilteredSplits(DataManager.COLUMN_TE + "= ?", new String[]{filterArg});
        } else if ("by Person".equals(filterName)) {
            return getFilteredSplits(DataManager.COLUMN_NAME + "= ?", new String[]{filterArg});
        }
        return getFilteredSplits(null, null);
    }


    public List<Result> getFilteredSplits(String filter, String[] filterArgs) {
        List<Result> splits = new ArrayList<Result>();

        Cursor cursor = database.query(DataManager.TABLE_SP, allColumns, filter, filterArgs, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Result st = cursorToString(cursor);
            splits.add(st);

            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return splits;
    }


    private Result cursorToString(Cursor c) {
        Result r = new Result(c.getString(0), 0 ,c.getString(1), c.getString(2), c.getInt(3));
        String[] strings = c.getString(4).split("/");
        for (String s : strings) {
            try {
                int i = Integer.parseInt(s);
                r.splitTime.add(i);
            } catch (NumberFormatException e) {
                Log.d("split parse","error: " + s);
            }
        }

        return r;
    }

    public void deleteFilteredData(String filterName, String filterArg) {
        if ("by Date".equals(filterName)) {
            deleteFilteredSplits(DataManager.COLUMN_TE + "= ?", new String[]{filterArg});
        } else {
            deleteFilteredSplits(null, null);
        }
    }

    public void deleteFilteredSplits(String filter, String[] filterArgs) {
            database.delete(DataManager.TABLE_SP, filter, filterArgs);
    }
}
