package com.darorl.converter.di;

import android.app.Application;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    Application app;

    public AppModule(Application application) {
        this.app = application;
    }

    @Provides
    Application provideApp() {
        return app;
    }
}

