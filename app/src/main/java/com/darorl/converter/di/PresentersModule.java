package com.darorl.converter.di;

import com.darorl.converter.repositories.RatesRepository;
import com.darorl.converter.ui.main.RatesContract;
import com.darorl.converter.ui.main.RatesPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PresentersModule {
    private RatesContract.presenter<RatesContract.view> ratesPresenter;

    public PresentersModule(RatesRepository repository) {
        ratesPresenter = new RatesPresenter(repository);
    }

    @Provides
    @Singleton
    RatesContract.presenter<RatesContract.view> provideRatesPresenter() {
        return ratesPresenter;
    }
}
