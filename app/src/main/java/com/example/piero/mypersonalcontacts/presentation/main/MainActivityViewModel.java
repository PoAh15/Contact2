package com.example.piero.mypersonalcontacts.presentation.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.piero.mypersonalcontacts.model.AppDatabase;
import com.example.piero.mypersonalcontacts.model.dao.ContactDao;
import com.example.piero.mypersonalcontacts.model.entity.Contact;
import com.example.piero.mypersonalcontacts.utils.BiFunction;
import com.example.piero.mypersonalcontacts.utils.ListUtils;
import com.example.piero.mypersonalcontacts.utils.LiveDataUtils;
import com.example.piero.mypersonalcontacts.utils.Predicate;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private final ContactDao contactDao;
    private MutableLiveData<String> filterLiveData = new MutableLiveData<>(); //default null

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        contactDao = AppDatabase.getInstance(application)
                .contactDao();
        filterLiveData.setValue("");
    }

    public void updateFilter(String filter){
        filterLiveData.postValue(filter);
    }

    public LiveData<List<Contact>> observeContacts(){
        return LiveDataUtils.combileLatestLiveData(
                contactDao.getAll(),// data
                filterLiveData,     // filter by
                filter());          // result
    }

    private BiFunction<List<Contact>, String, List<Contact>> filter(){
        return (list, filter) -> {
            if (TextUtils.isDigitsOnly(filter)){
                return ListUtils.filter(list, contact -> filterByNumber(filter).test(contact));
            }
            return ListUtils.filter(list, contact -> filterByName(filter).test(contact));
        };
    }

    private Predicate<Contact> filterByNumber(String filter){
        return value -> value.getPhone().toLowerCase().contains(filter);
    }

    private Predicate<Contact> filterByName(String filter){
        String filterLower = filter.toLowerCase();
        return value -> value.getFirstName().toLowerCase().contains(filterLower);
    }

    public void addContact(Contact contact) {
        contactDao.insertContact(contact);
    }
}
