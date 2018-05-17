package com.oritmalki.myheroapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.oritmalki.myheroapp.data.Hero;
import com.oritmalki.myheroapp.data.HeroRepository;

import java.util.List;

import javax.inject.Inject;

public class HeroViewModel extends ViewModel {

    private LiveData<List<Hero>> heroes;
    private HeroRepository heroRepo;


    @Inject
    public HeroViewModel(HeroRepository heroRepo) {
        this.heroRepo = heroRepo;
    }

    public void init() {
        if (this.heroes != null) {
            return;
        }

        heroes = heroRepo.getAllHeroes();
    }

    public LiveData<List<Hero>> getHeroes() {
        return heroes;
    }


}
