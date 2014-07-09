package com.muecke.tkcompanion.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.muecke.tkcompanion.com.muecke.tkcompanion.model.Person;

import java.util.ArrayList;
import java.util.List;

public class PersonDataSource {

    private final DBHelper dbHelper;
    private SQLiteDatabase database;

    public PersonDataSource(Context ctx) {
       dbHelper = new DBHelper(ctx);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Person createPerson(String name, String vname, String gruppe) {

        ContentValues values = new ContentValues();
        values.put("gruppe", gruppe);
        values.put("vname",vname);
        values.put("name",name);

        long insertId = database.insert("person", null, values);

        Person p = new Person();
        p.id=insertId;
        p.gruppe=gruppe;
        p.name=name;
        p.vname=vname;

        return p;
    }

    public List<Person> getAllPerson() {
        List<Person> persons = new ArrayList<Person>();

        return persons;
    }
}
