package com.darorl.converter;

import android.app.Application;

import com.darorl.converter.di.AppComponent;
import com.darorl.converter.di.AppModule;
import com.darorl.converter.di.DaggerAppComponent;
import com.darorl.converter.di.PresentersModule;
import com.darorl.converter.repositories.RatesRepositoryImpl;

public class App extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        this.appComponent = buildAppComponent();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    private AppComponent buildAppComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .presentersModule(new PresentersModule(new RatesRepositoryImpl()))
                .build();
    }

}
