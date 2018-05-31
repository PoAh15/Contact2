package com.example.piero.mypersonalcontacts.presentation.addContact;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.example.piero.mypersonalcontacts.model.AppDatabase;
import com.example.piero.mypersonalcontacts.model.dao.ContactDao;
import com.example.piero.mypersonalcontacts.model.entity.Contact;

/**
 * Created by piero on 17/05/18.
 */

public class AddContactViewModel extends AndroidViewModel {

    private final ContactDao dao;

    public AddContactViewModel(@NonNull Application application) {
        super(application);
        dao = AppDatabase.getInstance(application).contactDao();
    }

    public void addContact (Contact contact){dao.insertContact(contact);}
}
