package com.muecke.tkcompanion.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "swimmers.db";
    private static final int DATABASE_VERSION = 4;

    public static final String TABLE_PE = "persons";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_GROUP = "tgroup";
    public static final String COLUMN_TARGET_100F = "target_100f";
    public static final String COLUMN_TARGET_100B = "target_100b";
    public static final String COLUMN_TARGET_100R = "target_100r";
    public static final String COLUMN_TARGET_100S = "target_100s";
    public static final String COLUMN_TARGET_200F = "target_200f";
    public static final String COLUMN_TARGET_200B = "target_200b";
    public static final String COLUMN_TARGET_200R = "target_200r";
    public static final String COLUMN_TARGET_200S = "target_200s";
    public static final String COLUMN_TARGET_400F = "target_400f";
    public static final String COLUMN_TARGET_800F = "target_800f";
    public static final String COLUMN_TARGET_1500F = "target_1500f";
    public static final String COLUMN_TARGET_50F = "target_50f";
    public static final String COLUMN_TARGET_50B = "target_50b";
    public static final String COLUMN_TARGET_50R = "target_50r";
    public static final String COLUMN_TARGET_50S = "target_50s";
    private static final String CREATE_TABLE_PE = "create table " + TABLE_PE + "( " +
            COLUMN_NAME + " text primary key, " +
            COLUMN_GROUP + " text not null, " +
            COLUMN_TARGET_50F + " integer, " +
            COLUMN_TARGET_50B + " integer, " +
            COLUMN_TARGET_50R + " integer, " +
            COLUMN_TARGET_50S + " integer, " +
            COLUMN_TARGET_100F + " integer, " +
            COLUMN_TARGET_100B + " integer, " +
            COLUMN_TARGET_100R + " integer, " +
            COLUMN_TARGET_100S + " integer, " +
            COLUMN_TARGET_200F + " integer, " +
            COLUMN_TARGET_200B + " integer, " +
            COLUMN_TARGET_200R + " integer, " +
            COLUMN_TARGET_200S + " integer, " +
            COLUMN_TARGET_400F + " integer, " +
            COLUMN_TARGET_800F + " integer, " +
            COLUMN_TARGET_1500F + " integer " +
            " ); ";
    public static final String TABLE_PR = "presence";
    public static final String COLUMN_TE = "session";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_DURATION = "duration";
    private static final String CREATE_TABLE_AV = "create table " + TABLE_PR + "( " +
            COLUMN_NAME + " text not null, " +
            COLUMN_TE + " text not null " +
            " );";
    public static final String TABLE_IR = "interval_results";
    public static final String COLUMN_COMP = "competition";
    public static final String COLUMN_SPLIT = "split";
    public static final String COLUMN_THRESHOLD = "threshold";
    public static final String COLUMN_AVERGAE = "average";
    public static final String COLUMN_TARGET = "target";
    public static final String COLUMN_SENDOFF = "sendoff";
    private static final String CREATE_TABLE_IR = "CREATE TABLE " + TABLE_IR + "( " +
            COLUMN_NAME + " text not null, " +
            COLUMN_TE + " text not null, " +
            COLUMN_COMP + " text not null, " +
            COLUMN_SPLIT + " text, " +
            COLUMN_AVERGAE + " integer " +
            " );";

    public static final String TABLE_SP = "split_results";
    public static final String COLUMN_TOTAL = "total";

    private static final String CREATE_TABLE_SP = "CREATE TABLE " + TABLE_SP + "( " +
            COLUMN_NAME + " text not null, " +
            COLUMN_TE + " text not null, " +
            COLUMN_COMP + " text not null, " +
            COLUMN_SPLIT + " text, " +
            COLUMN_TOTAL + " integer " +
            " );";

    public DataManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_PE);
        database.execSQL(CREATE_TABLE_AV);
        database.execSQL(CREATE_TABLE_IR);
        database.execSQL(CREATE_TABLE_SP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DataManager.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        if (newVersion == 4) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_IR);
            db.execSQL(CREATE_TABLE_IR);

        } else {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PR);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_IR);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SP);

            onCreate(db);
        }
    }
}
