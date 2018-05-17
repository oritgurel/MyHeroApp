package com.oritmalki.myheroapp.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.oritmalki.myheroapp.R;
import com.oritmalki.myheroapp.data.Hero;
import com.oritmalki.myheroapp.viewmodel.HeroViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    HeroAdapter adapter;
    List<LiveData<Hero>> heroes;


//    private HeroViewModel viewModel;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private HeroViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.configureDagger();
        initRecyclerView();
        configureViewModel();



    }

    private void configureViewModel() {
        this.viewModel = ViewModelProviders.of(this, viewModelFactory).get(HeroViewModel.class);
        viewModel.init();
        viewModel.getHeroes().observe(this, heroes -> {
            if (heroes != null) {
                adapter = new HeroAdapter(heroes, getApplicationContext());
                recyclerView.setAdapter(adapter);

                //update title
                initToolbar(heroes);


            }
        });
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.main_recyclerview);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
    }

    private void initToolbar(List<Hero> heroes) {
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ImageView titleImage = findViewById(R.id.title_image);
        Glide.with(this).load(heroes.get(0).getImage()).into(titleImage);
        getSupportActionBar().setTitle(heroes.get(0).getTitle().toString());

    }




    // private void updateUI(@Nu
}
