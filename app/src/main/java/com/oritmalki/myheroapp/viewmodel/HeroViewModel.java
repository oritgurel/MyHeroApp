package com.oritmalki.myheroapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;

import com.oritmalki.myheroapp.data.Hero;
import com.oritmalki.myheroapp.data.HeroRepository;

import java.util.List;

import javax.inject.Inject;

public class HeroViewModel extends ViewModel {

    private LiveData<List<Hero>> heroes;
    private HeroRepository heroRepo;
    private final MediatorLiveData<List<Hero>> mObservableHeroes;


    @Inject
    public HeroViewModel(HeroRepository heroRepo) {
        this.heroRepo = heroRepo;
        mObservableHeroes = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableHeroes.setValue(null);
        LiveData<List<Hero>> heroes = heroRepo.getAllHeroes();
        mObservableHeroes.addSource(heroes, mObservableHeroes::setValue);


    }


    public void init() {
        if (this.mObservableHeroes != null) {
            return;
        }

        heroes = heroRepo.getAllHeroes();
    }

//    public LiveData<List<Hero>> getHeroes() {
//        return this.heroes;
//    }

    public void saveHero(Hero hero) {
        heroRepo.saveHero(hero);
    }

    /**
     * Expose the LiveData Products query so the UI can observe it.
     */
    public LiveData<List<Hero>> getHeroes() {
        return mObservableHeroes;
    }


    public Hero getFavorite() {
        Hero favHeroName = null;
        for (Hero hero : heroes.getValue()) {
            if (hero.isFavorite())
                favHeroName = hero;
            else {
                favHeroName = mObservableHeroes.getValue().get(0);
            }
        }
        return favHeroName;
    }
}
