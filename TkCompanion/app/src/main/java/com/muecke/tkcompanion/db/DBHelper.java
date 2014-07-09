package com.muecke.tkcompanion.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tkswimcompanion.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_PERSON = "create table person ( " +
            "id integer primary key autoincrement," +
            "gruppe text," +
            "vname,name text " +
            ");";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_PERSON);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i2) {
        // not really an upgrade but should work for now
        database.execSQL("DROP TABLE IF EXISTS person");
        onCreate(database);
    }
}
