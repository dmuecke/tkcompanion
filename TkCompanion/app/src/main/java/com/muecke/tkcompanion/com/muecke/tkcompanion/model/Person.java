package com.muecke.tkcompanion.com.muecke.tkcompanion.model;

public class Person {
    public long id;
    public String gruppe;
    public String vname;
    public String name;

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return vname + " " + name + " " + gruppe;
    }
}
