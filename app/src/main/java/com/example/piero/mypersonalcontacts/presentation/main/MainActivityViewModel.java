package com.example.piero.mypersonalcontacts.presentation.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.piero.mypersonalcontacts.model.AppDatabase;
import com.example.piero.mypersonalcontacts.model.dao.ContactDao;
import com.example.piero.mypersonalcontacts.model.entity.Contact;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private final ContactDao contactDao;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        contactDao = AppDatabase.getInstance(application)
                .contactDao();
    }

    public LiveData<List<Contact>> observeContacts(){
        return contactDao.getAll();
    }

    public void addContact(Contact contact) {
        contactDao.insertContact(contact);
    }
}
