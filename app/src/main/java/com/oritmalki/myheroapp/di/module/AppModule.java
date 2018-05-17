package com.oritmalki.myheroapp.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oritmalki.myheroapp.api.IHeroAPIService;
import com.oritmalki.myheroapp.data.AppDatabase;
import com.oritmalki.myheroapp.data.HeroDao;
import com.oritmalki.myheroapp.data.HeroRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class AppModule {

    //database injection

    @Provides
    @Singleton
    AppDatabase provideDatabase(Application application) {
        return Room.databaseBuilder(application, AppDatabase.class, "MyHeroes.db").build();
    }

    @Provides
    @Singleton
    HeroDao provideHeroDao(AppDatabase database) {
        return database.heroDao();
    }

    //repository injection

    @Provides
    Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Provides
    @Singleton
    HeroRepository provideHeroRepository(IHeroAPIService heroAPIService, HeroDao heroDao, Executor executor) {
        return new HeroRepository(heroAPIService, heroDao, executor);
    }

    //network injection

    private static String BASE_URL = "https://heroapps.co.il/";

    @Provides
    Gson provideGson() {return new GsonBuilder().create();}

    @Provides
    Retrofit provideRetrofit(Gson gson) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .baseUrl(BASE_URL)
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    IHeroAPIService provideApiWebservice(Retrofit restAdapter) {
        return restAdapter.create(IHeroAPIService.class);
    }
}
