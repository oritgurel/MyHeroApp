package com.oritmalki.myheroapp.api;

import com.oritmalki.myheroapp.data.Hero;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IHeroAPIService {

    String endPoint = "employee-tests/android/androidexam.json";

    @GET(endPoint)
    Call<List<Hero>> getHeroes();

}
