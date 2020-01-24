package com.darorl.converter.di;

import com.darorl.converter.ui.main.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, PresentersModule.class, RepositoriesModule.class, ApiModule.class})
public interface AppComponent {
    void inject(MainActivity activity);
}
