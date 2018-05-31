package com.example.piero.mypersonalcontacts.model.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.piero.mypersonalcontacts.model.entity.Contact;

import java.util.List;

@Dao
public abstract class ContactDao {

    @Query("SELECT * FROM contact ORDER BY firstName")
    public abstract LiveData<List<Contact>> getAll();

    @Query("SELECT * FROM contact WHERE id = :contactId")
    public abstract LiveData<Contact> getById(int contactId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertContact(Contact contact);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void updateContact(Contact contact);

    @Delete
    public abstract void deleteUser(Contact contact);

    @Query("DELETE FROM contact WHERE id = :id")
    public abstract void deleteUserByid(int id);

}
