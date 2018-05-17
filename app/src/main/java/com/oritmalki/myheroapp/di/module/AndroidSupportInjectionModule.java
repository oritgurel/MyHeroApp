package com.oritmalki.myheroapp.di.module;

import android.support.v4.app.Fragment;

import java.util.Map;

import dagger.Module;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector.Factory;
import dagger.internal.Beta;
import dagger.multibindings.Multibinds;

@Beta
@Module(includes = AndroidInjectionModule.class)
public abstract class AndroidSupportInjectionModule {
    @Multibinds
    abstract Map<Class<? extends Fragment>, Factory<? extends Fragment>>
    supportFragmentInjectorFactories();

    private AndroidSupportInjectionModule() {}
}