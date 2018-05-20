package com.oritmalki.myheroapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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
    private ImageSwitcher titleImage;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private Hero lastSelected;
    private HeroHolder lastSelectedHeart;
    private String actionBarTitle;
    private String actionBarImage;
    private CoordinatorLayout coordinatorLayout;
    private AppBarLayout appBarLayout;
    private int topOfScreen;
    private int bottomOfScreen;
    private Hero favHero;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FrameLayout container;
    private String currentImageUrl;



//    private HeroViewModel viewModel;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private HeroViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureImageSwitcher();

        //configure photo fragment
        titleImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentImageUrl != null) {
                    android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                    ImageFragment imageFragment = ImageFragment.getInstance(currentImageUrl);
                    fm.beginTransaction().add(R.id.container, imageFragment).addToBackStack(imageFragment.getTag()).commit();
                    appBarLayout.setExpanded(false);

                }
            }
        });

        configureActionBar();
        this.configureDagger();
        initRecyclerView();
        configureViewModel();



        appBarLayout = findViewById(R.id.main_appBar);
        coordinatorLayout = findViewById(R.id.coordinator);
        topOfScreen = coordinatorLayout.getTop();
        bottomOfScreen = coordinatorLayout.getBottom();



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
                        currentImageUrl = favHero.getImage();

                    } else {
                        actionBarTitle = heroes.get(0).getTitle();
                        actionBarImage = heroes.get(0).getImage();
                    }


                        collapsingToolbarLayout.setTitle(actionBarTitle);
                        setImage(actionBarImage);

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


    //callback from adapter
    @Override
    public void OnHeroSelected(Hero hero, HeroHolder heroHolder) {
        actionBarTitle = hero.getTitle();
        collapsingToolbarLayout.setTitle(actionBarTitle);

//            setImage(hero.getImage());

        setFavorite(hero, heroHolder);
        currentImageUrl = hero.getImage();

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
                    YoYo.with(Techniques.FadeIn).duration(700).playOn(holder.heart_ic);
                    holder.heart_ic.setVisibility(View.VISIBLE);
                    collapsingToolbarLayout.setTitle(hero.getTitle());
                    setImage(hero.getImage());
                }
            }

        }
    }

    public void configureImageSwitcher() {
        //configure image switcher
        Animation in = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
        titleImage = findViewById(R.id.title_image);
        titleImage.setFactory(new ViewFactory() {

            public View makeView() {
                ImageView imageView = new ImageView(MainActivity.this);
                imageView.setScaleType(ScaleType.CENTER_CROP);
                LayoutParams params = new ImageSwitcher.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                imageView.setLayoutParams(params);
                return imageView;

            }
        });
        titleImage.setInAnimation(in);
        titleImage.setOutAnimation(out);
    }

    public void setImage(String imageUrl) {
        //glide request options:
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC);

        Glide.with(this).asBitmap().load(imageUrl).apply(requestOptions).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                titleImage.setImageDrawable(new BitmapDrawable(getResources(), resource));
            }
        });
    }

    //try to find collapsing toolbar textview for animation
    private TextView getToolbarTitleTextView() {
        int childCount = toolbar.getChildCount();
        TextView textView = null;
        for (int i = 0; i < childCount; i++) {
            View child = toolbar.getChildAt(i);
            if (child instanceof TextView) {
                textView = (TextView) child;
            }
        }

        return textView;
    }

    public void configureActionBar() {
        toolbar = findViewById(R.id.main_toolbar);
        collapsingToolbarLayout = findViewById(R.id.main_collapsing);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

        collapsingToolbarLayout.setTitle(actionBarTitle);
        setImage(actionBarImage);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (appBarLayout != null)
        appBarLayout.setExpanded(true);
    }
}
