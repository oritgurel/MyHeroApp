package com.oritmalki.myheroapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
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
    private CoordinatorLayout coordinatorLayout;
    private AppBarLayout appBarLayout;
    private int topOfScreen;
    private Hero favHero;
    private CollapsingToolbarLayout collapsingToolbarLayout;



//    private HeroViewModel viewModel;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private HeroViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //configure actionbar
        toolbar = findViewById(R.id.main_toolbar);
        collapsingToolbarLayout = findViewById(R.id.main_collapsing);
        titleImage = findViewById(R.id.title_image);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);



        collapsingToolbarLayout.setTitle(actionBarTitle);
        Glide.with(getApplicationContext()).load(actionBarImage).into(titleImage);




        this.configureDagger();
        initRecyclerView();
        configureViewModel();



        appBarLayout = findViewById(R.id.main_appBar);
        coordinatorLayout = findViewById(R.id.coordinator);
        topOfScreen = coordinatorLayout.getTop();


    }











    private void configureViewModel() {
        this.viewModel = ViewModelProviders.of(this, viewModelFactory).get(HeroViewModel.class);
//         viewModel.init();
        viewModel.getHeroes().observe(this, new Observer<List<Hero>>() {
            @Override
            public void onChanged(@Nullable List<Hero> heroes) {
                if (heroes != null && heroes.size() != 0) {
                    adapter = new HeroAdapter(MainActivity.this.getApplicationContext(), MainActivity.this::OnHeroSelected);
                    adapter.setHeroesList(heroes, MainActivity.this.getApplicationContext());
                    recyclerView.setAdapter(adapter);

                    for (Hero hero : heroes) {
                        if (hero.isFavorite())
                            favHero = lastSelected = hero;

                    }

                    if (favHero != null) {
                        actionBarTitle = favHero.getTitle();
                        actionBarImage = favHero.getImage();
                    } else {
                        actionBarTitle = heroes.get(0).getTitle();
                        actionBarImage = heroes.get(0).getImage();
                    }


                        collapsingToolbarLayout.setTitle(actionBarTitle);
                        Glide.with(getApplicationContext()).load(actionBarImage).into(titleImage);
                    }

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
        collapsingToolbarLayout.setTitle(actionBarTitle);
        Glide.with(this).load(hero.getImage()).into(titleImage);

        setFavorite(hero, heroHolder);

        appBarLayout.setExpanded(true);
        coordinatorLayout.scrollTo(0, topOfScreen);

    }

    private void setFavorite(Hero hero, HeroHolder holder) {
        if (lastSelected !=null) {
            lastSelected.setFavorite(false);
            if (lastSelectedHeart!=null) {
                lastSelectedHeart = holder;
            }
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
            if (holder != null) {
                holder.heart_ic.setVisibility(View.GONE);
            } else  {
                if (holder != null) {
                    holder.heart_ic.setVisibility(View.VISIBLE);
                    collapsingToolbarLayout.setTitle(hero.getTitle());
                    Glide.with(this).load(hero.getImage()).into(titleImage);
                }
            }

        }
    }




    // private void updateUI(@Nu
}
