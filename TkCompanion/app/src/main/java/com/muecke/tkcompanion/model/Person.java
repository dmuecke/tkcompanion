package com.muecke.tkcompanion.model;

import java.io.Serializable;

public class Person implements Serializable {

    public Person() {

    }

    public Person(Person person) {
        this.group=person.getGroup();
        this.name=person.getName();
        this.present=person.isPresent();
    }


    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String group;
    private String name;

    private boolean present;

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }
}
