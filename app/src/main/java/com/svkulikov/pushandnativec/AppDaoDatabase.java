package com.svkulikov.pushandnativec;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = { MessageData.class }, version = 1, exportSchema = false)

public abstract class AppDaoDatabase extends RoomDatabase {
    public abstract AppDao getDaoDatabase();

}
