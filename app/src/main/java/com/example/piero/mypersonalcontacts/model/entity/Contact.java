package com.example.piero.mypersonalcontacts.model.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;

/**
 * Created by piero on 07/05/18.
 */

@Entity(tableName = "contact")
public class Contact {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private final String firstName;
    private final String lastName;
    private final String phone;
    private final String email;
    private final String company;
    private final String address;
    private final String pictures;

    public Contact(String firstName, String lastName, String phone, String email, String company, String address, String pictures) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.company = company;
        this.address = address;
        this.pictures = pictures;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() { return email; }

    public String getCompany() { return company; }

    public String getAddress() { return address; }

    public String getPictures() { return pictures; }
}
