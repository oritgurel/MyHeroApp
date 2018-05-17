package com.oritmalki.myheroapp.api;

import android.util.Log;

import com.oritmalki.myheroapp.data.Hero;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HeroServiceGenerator {

    static final String BASE_URL = "https://heroapps.co.il/";

    static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    static Retrofit retrofit;
    static IHeroAPIService heroAPIService;

    private static final HeroServiceGenerator ourInstance = new HeroServiceGenerator();

    public static HeroServiceGenerator getInstance() {
        return ourInstance;
    }

    private HeroServiceGenerator() {
        retrofit = retrofitBuilder.build();
        heroAPIService = retrofit.create(IHeroAPIService.class);
    }

    public List<Hero> getHeroes() {
        final List<Hero> heroesList = new ArrayList<>();
        Call<List<Hero>> heroResponseCall = heroAPIService.getHeroes();
        heroResponseCall.enqueue(new Callback<List<Hero>>() {
            @Override
            public void onResponse(Call<List<Hero>> call, Response<List<Hero>> response) {

                List<Hero> heroesList = response.body();

            }

            @Override
            public void onFailure(Call<List<Hero>> call, Throwable t) {

                Log.e("NetworkError: ", "Something went wrong. " + t.getMessage());
                //TODO toast
            }
        });

        return heroesList;
    }
}
