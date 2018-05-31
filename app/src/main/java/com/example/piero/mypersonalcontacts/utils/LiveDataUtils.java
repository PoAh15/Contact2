package com.example.piero.mypersonalcontacts.utils;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

public class LiveDataUtils {

    public static <T, R, S> LiveData<S> combileLatestLiveData(
            @NonNull LiveData<T> firstLiveData,
            @NonNull LiveData<R> secondLiveData,
            @NonNull BiFunction<T, R, S> func){

        final MediatorLiveData<S> result = new MediatorLiveData<>();

        Wrapper<T> firstValue = new Wrapper<>();
        Wrapper<R> secondValue = new Wrapper<>();

        result.addSource(firstLiveData, t -> {
            firstValue.value = t;

            if (secondValue.isNotEmpty()){
                result.setValue(func.apply(t, secondValue.value));
            }
        });
        result.addSource(secondLiveData, r -> {
            secondValue.value = r;

            if (firstValue.isNotEmpty()){
                result.setValue(func.apply(firstValue.value, r));
            }
        });
        return result;
    }



    private static class Wrapper<T> {

        public T value = null;

        public boolean isEmpty(){
            return value == null;
        }

        public boolean isNotEmpty(){
            return !isEmpty();
        }

    }

}
