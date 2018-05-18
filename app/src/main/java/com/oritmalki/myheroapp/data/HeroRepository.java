package com.oritmalki.myheroapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.widget.Toast;

import com.oritmalki.myheroapp.App;
import com.oritmalki.myheroapp.api.IHeroAPIService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class HeroRepository {

    private static int FRESH_TIMEOUT_IN_MINUTES = 3;

    private static HeroRepository sInstance;
    private final IHeroAPIService heroAPIService;
    private final HeroDao heroDao;
    private final Executor executor;
    LiveData<List<Hero>> liveHeroList;

    private MediatorLiveData<List<Hero>> mObservableHeroes;


@Inject
    public HeroRepository(IHeroAPIService heroAPIService, HeroDao heroDao, Executor executor) {
        this.heroAPIService = heroAPIService;
        this.heroDao = heroDao;
        this.executor = executor;


//        mObservableHeroes = new MediatorLiveData<>();
//        mObservableHeroes.addSource((LiveData<List<Hero>>) heroAPIService.getHeroes(), new Observer<List<Hero>>() {
//
//            @Override
//            public void onChanged(@Nullable List<Hero> heroEntities) {
//
//            }
//        });

    }

    public void saveHero(Hero hero) {
    executor.execute(() -> {
        heroDao.save(hero);
    });
    }

    public LiveData<Hero> getHero(String title) {
        return heroDao.getHeroByName(title);
    }

    public LiveData<List<Hero>> getAllHeroes() {
    executor.execute(() -> {
            refreshHeroes();
        }
    );

        return heroDao.getAllHeroes();
    }

    private void refreshHeroes() {
    executor.execute(() -> {

        //check if hero was inserted for the first time and got a date stamp
        boolean heroExists = (heroDao.hasHero(getMaxRefreshTime(new Date())) != null);
        //if hero does not exists yet, update from web
//        if (!heroExists) {
            heroAPIService.getHeroes().enqueue(new Callback<List<Hero>>() {
                @Override
                public void onResponse(Call<List<Hero>> call, Response<List<Hero>> response) {
                    Toast.makeText(App.context, "Data refreshed from network!", Toast.LENGTH_LONG).show();
                    executor.execute(() -> {
                        for (Hero hero : response.body()) {

                            hero.setLastRefresh(new Date());
                            heroDao.save(hero);
                        }

                    });
                }

                @Override
                public void onFailure(Call<List<Hero>> call, Throwable t) {

                }
            });
//        }
    });
    }

    private Date getMaxRefreshTime(Date currentDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.MINUTE, -FRESH_TIMEOUT_IN_MINUTES);
        return cal.getTime();
    }
}
