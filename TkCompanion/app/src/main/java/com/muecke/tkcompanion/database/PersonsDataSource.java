package com.muecke.tkcompanion.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class PersonsDataSource {
    // Database fields
    private SQLiteDatabase database;
    private DataManager dbHelper;
    private String[] allColumns = { DataManager.COLUMN_ID, DataManager.COLUMN_NAME, DataManager.COLUMN_GROUP };

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
        long insertId = database.insert(DataManager.TABLE_PERSONS, null, values);
        Cursor cursor = database.query(DataManager.TABLE_PERSONS,
                allColumns, DataManager.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Person newPerson = cursorToComment(cursor);
        cursor.close();
        return newPerson;
    }

    public void deletePerson(Person person) {
        long id = person.getId();
        System.out.println("Person deleted with id: " + id);
        database.delete(DataManager.TABLE_PERSONS, DataManager.COLUMN_ID + " = " + id, null);
    }


    public List<Person> getAllPersons() {
        List<Person> persons = new ArrayList<Person>();

        Cursor cursor = database.query(DataManager.TABLE_PERSONS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Person person = cursorToComment(cursor);
            persons.add(person);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return persons;
    }

    private Person cursorToComment(Cursor cursor) {
        Person person = new Person();
        person.setId(cursor.getLong(0));
        person.setName(cursor.getString(1));
        person.setGroup(cursor.getString(2));
        return person;
    }
}
