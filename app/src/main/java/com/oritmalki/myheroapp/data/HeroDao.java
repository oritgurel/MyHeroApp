package com.oritmalki.myheroapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface HeroDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(Hero hero);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Hero> heroes);

    @Query("SELECT * FROM hero")
    LiveData<List<Hero>> getAllHeroes();

    @Query("SELECT * FROM hero where title = :title")
    LiveData<Hero> getHeroByName(String title);

//    @Query("SELECT * FROM hero where title = :title AND lastRefresh > :lastRefreshMax LIMIT 1")
//    Hero hasHero(String title, Date lastRefreshMax);

    @Query("SELECT * FROM hero where lastRefresh > :lastRefreshMax LIMIT 1")
    List<Hero> hasHeroes(Date lastRefreshMax);

}
