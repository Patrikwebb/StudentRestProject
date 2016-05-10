package se.yrgo.java15.patrik.studentapp.storage;

import android.content.Context;

public class StorageManager {

    public static LocalStorage getStorage(Context context){
        return DBLocal.getInstance(context);

        //TODO Add Retrofiot
    }
}
