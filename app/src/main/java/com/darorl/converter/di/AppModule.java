package com.darorl.converter.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Application app;

    public AppModule(Application application) {
        this.app = application;
    }

    @Provides
    @Singleton
    Application provideApp() {
        return app;
    }
}

