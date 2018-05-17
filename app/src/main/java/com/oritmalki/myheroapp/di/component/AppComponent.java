package com.oritmalki.myheroapp.di.component;

import android.app.Application;

import com.oritmalki.myheroapp.App;
import com.oritmalki.myheroapp.di.module.ActivityModule;
import com.oritmalki.myheroapp.di.module.AndroidSupportInjectionModule;
import com.oritmalki.myheroapp.di.module.AppModule;
import com.oritmalki.myheroapp.di.module.ViewModelModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, AndroidSupportInjectionModule.class, ViewModelModule.class, ActivityModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }

    void inject(App app);
}
