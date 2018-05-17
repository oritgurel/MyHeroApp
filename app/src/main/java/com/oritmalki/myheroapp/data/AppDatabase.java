package com.oritmalki.myheroapp.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;


@TypeConverters({DateConverter.class, AbilitiesConverter.class})
@Database(entities = Hero.class, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase sInstance;

    public abstract HeroDao heroDao();

}
