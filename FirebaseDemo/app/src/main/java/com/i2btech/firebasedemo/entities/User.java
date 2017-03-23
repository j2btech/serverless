package com.i2btech.firebasedemo.entities;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by jflorez on 21-03-17.
 */

@IgnoreExtraProperties
public class User {

    private String name;
    private String lastName;
    private String birthday;

    public User() {

    }

    public User(String name, String lastName, String birthday) {
        this.name = name;
        this.lastName = lastName;
        this.birthday = birthday;
    }

    public String getName() {
        return this.name;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getBirthday() {
        return this.birthday;
    }
}
