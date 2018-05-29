package com.example.piero.mypersonalcontacts.presentation.detail;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.piero.mypersonalcontacts.model.AppDatabase;
import com.example.piero.mypersonalcontacts.model.dao.ContactDao;
import com.example.piero.mypersonalcontacts.model.entity.Contact;

/**
 * Created by piero on 07/05/18.
 */

public class DetailContactActivityViewModel extends AndroidViewModel {

    private ContactDao contactDao;

    public DetailContactActivityViewModel(@NonNull Application application) {
        super(application);
        contactDao = AppDatabase.getInstance(application)
                .contactDao();
    }

    public LiveData<Contact> observeContact(int id){
        return contactDao.getById(id);
    }

}
