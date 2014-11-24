package com.muecke.tkcompanion.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.muecke.tkcompanion.model.Person;

import java.util.ArrayList;
import java.util.List;

public class PersonsDataSource {
    // Database fields
    private SQLiteDatabase database;
    private DataManager dbHelper;
    private String[] allColumns = { DataManager.COLUMN_NAME, DataManager.COLUMN_GROUP };

    public PersonsDataSource(Context context) {
        dbHelper = new DataManager(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Person createPerson(String name, String group) {
        ContentValues values = new ContentValues();
        values.put(DataManager.COLUMN_NAME, name);
        values.put(DataManager.COLUMN_GROUP, group);
        try {
            database.insert(DataManager.TABLE_PE, null, values);
        } catch (SQLiteConstraintException e) {
            Log.w("createPerson", e.getMessage());
        }
        Cursor cursor = database.query(DataManager.TABLE_PE,
                allColumns, DataManager.COLUMN_NAME + " = ?" , new String[] {name}, null, null, null);
        cursor.moveToFirst();
        Person newPerson = cursorToPerson(cursor);
        cursor.close();
        return newPerson;
    }

    public void insertPerson(ContentValues values) throws SQLException {
            database.insert(DataManager.TABLE_PE, null, values);
    }

    public void deletePerson(Person person) {
        String name = person.getName();
        System.out.println("Person deleted with name: " + name);
        database.delete(DataManager.TABLE_PE, DataManager.COLUMN_NAME + "= ?", new String[]{name});
    }


    public List<Person> getAllPersons() {
        List<Person> persons = new ArrayList<Person>();

        Cursor cursor = database.query(DataManager.TABLE_PE,
                allColumns, null, null, null, null, DataManager.COLUMN_NAME);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Person person = cursorToPerson(cursor);
            persons.add(person);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return persons;
    }

    private Person cursorToPerson(Cursor cursor) {
        Person person = new Person();
        person.setName(cursor.getString(0));
        person.setGroup(cursor.getString(1));
        return person;
    }

    public String[] createTableColumns(String[] split) {
        String[] headers = new String[split.length];

        return new String[]{ DataManager.COLUMN_NAME, DataManager.COLUMN_GROUP,
                DataManager.COLUMN_TARGET_50F,DataManager.COLUMN_TARGET_50B, DataManager.COLUMN_TARGET_50R,DataManager.COLUMN_TARGET_50S,
                DataManager.COLUMN_TARGET_100F,DataManager.COLUMN_TARGET_100B, DataManager.COLUMN_TARGET_100R,DataManager.COLUMN_TARGET_100S,
                DataManager.COLUMN_TARGET_200F,DataManager.COLUMN_TARGET_200B, DataManager.COLUMN_TARGET_200R,DataManager.COLUMN_TARGET_200S
        };
    }
}
