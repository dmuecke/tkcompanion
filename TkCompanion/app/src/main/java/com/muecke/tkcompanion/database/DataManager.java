package com.muecke.tkcompanion.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "swimmers.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PERSONS = "persons";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_GROUP = "group";
    public static final String COLUMN_ID = "id";
    private static final String DATABASE_CREATE = "create table " + TABLE_PERSONS + "( " + COLUMN_ID + " integer primary key autoincrement," +
            " " + COLUMN_GROUP + " text not null, " + COLUMN_NAME + " text not null); ";
    public DataManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DataManager.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSONS);
        onCreate(db);
    }
}
