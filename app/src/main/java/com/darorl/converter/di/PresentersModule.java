package com.darorl.converter.di;

import com.darorl.converter.repositories.RatesRepository;
import com.darorl.converter.ui.main.RatesContract;
import com.darorl.converter.ui.main.RatesPresenter;

import dagger.Module;
import dagger.Provides;

@Module (
        includes = RepositoriesModule.class
)
public class PresentersModule {

    @Provides
    RatesContract.presenter<RatesContract.view> provideRatesPresenter(RatesRepository repository) {
        return new RatesPresenter(repository);
    }
}
