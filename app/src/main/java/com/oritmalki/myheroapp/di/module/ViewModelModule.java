package com.oritmalki.myheroapp.di.module;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.oritmalki.myheroapp.di.key.ViewModelKey;
import com.oritmalki.myheroapp.viewmodel.FactoryViewModel;
import com.oritmalki.myheroapp.viewmodel.HeroViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HeroViewModel.class)
    abstract ViewModel bindHeroViewModel(HeroViewModel repoViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(FactoryViewModel factory);
}