package com.oritmalki.myheroapp.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.oritmalki.myheroapp.R;
import com.oritmalki.myheroapp.data.Hero;
import com.oritmalki.myheroapp.ui.HeroAdapter.HeroHolder;
import com.oritmalki.myheroapp.viewmodel.HeroViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity implements AdapterCallback {

    private RecyclerView recyclerView;
    private HeroAdapter adapter;
    private List<Hero> localHeroReference;
    private ImageView titleImage;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private Hero lastSelected;
    private HeroHolder lastSelectedHeart;
    private String actionBarTitle;
    private String actionBarImage;



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

        //configure actionbar
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

        //get initial saved data
//        for (Hero hero : localHeroReference) {
//            if (hero.isFavorite()) {
//                actionBarTitle = hero.getTitle();
//                actionBarImage = hero.getImage();
//            }
//        }

        actionBar.setTitle(actionBarTitle);
        titleImage = findViewById(R.id.title_image);
        Glide.with(this).load(actionBarImage).into(titleImage);



    }











    private void configureViewModel() {
        this.viewModel = ViewModelProviders.of(this, viewModelFactory).get(HeroViewModel.class);
         viewModel.init();
        viewModel.getHeroes().observe(this, (List<Hero> heroes) -> {
            if (heroes != null && heroes.size() != 0) {
                adapter = new HeroAdapter(getApplicationContext(), this::OnHeroSelected);
                adapter.setHeroesList(heroes, getApplicationContext());
                recyclerView.setAdapter(adapter);

                localHeroReference = new ArrayList<>();
                for (Hero hero : heroes) {
                    localHeroReference.add(hero);
                }



                //update title
//                initToolbar(heroes);


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
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);


        Glide.with(this).load(heroes.get(0).getImage()).into(titleImage);
        getSupportActionBar().setTitle(heroes.get(0).getTitle());

    }

    //callback from adapter
    @Override
    public void OnHeroSelected(Hero hero, HeroHolder heroHolder) {
        actionBarTitle = hero.getTitle();
        actionBar.setTitle(actionBarTitle);
        Glide.with(this).load(hero.getImage()).into(titleImage);

        setFavorite(hero, heroHolder);

    }

    private void setFavorite(Hero hero, HeroHolder holder) {
        if (lastSelected !=null) {
            lastSelected.setFavorite(false);
            setHeartVisibility(lastSelected, lastSelectedHeart);
            viewModel.saveHero(lastSelected);

        }

        hero.setFavorite(true);
        setHeartVisibility(hero, holder);
        viewModel.saveHero(hero);
        lastSelected = hero;
        lastSelectedHeart = holder;


    }

    private void setHeartVisibility(Hero hero, HeroHolder holder) {
        if (!hero.isFavorite()) {
            holder.heart_ic.setVisibility(View.GONE);
        } else {
            holder.heart_ic.setVisibility(View.VISIBLE);
        }
    }




    // private void updateUI(@Nu
}
