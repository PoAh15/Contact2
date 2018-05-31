package com.example.piero.mypersonalcontacts.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

    public static <T> List<T> filter(List<T> list, Predicate<T> predicate){
        ArrayList<T> result = new ArrayList<>();
        for (T t : list) {
            if (predicate.test(t)){
                result.add(t);
            }
        }
        return result;
    }

}
