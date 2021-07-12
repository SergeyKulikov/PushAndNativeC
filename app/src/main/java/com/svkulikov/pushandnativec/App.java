package com.svkulikov.pushandnativec;

import android.app.Application;

import androidx.room.Room;

public class App extends Application {
    public static App instance;
    private AppDaoDatabase appDaoDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appDaoDatabase = Room.databaseBuilder(getApplicationContext(), AppDaoDatabase.class, getPackageName()+"_db").
                // .allowMainThreadQueries().
                build();
    }

    public AppDaoDatabase getAppDatabase() {
        return appDaoDatabase;
    }

    public static App getInstance() {
        return instance;
    }
}




