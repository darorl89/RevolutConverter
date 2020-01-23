package com.darorl.converter.ui.main;

import com.darorl.converter.repositories.RatesRepository;
import com.darorl.converter.model.RatesData;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RatesPresenter implements RatesContract.presenter<RatesContract.view> {

    private RatesRepository ratesRepository;

    private RatesContract.view view;

    private CompositeDisposable disposable;

    public RatesPresenter(RatesRepository ratesRepository) {
        this.ratesRepository = ratesRepository;
        this.disposable = new CompositeDisposable();
    }

    @Override
    public void requestRates() {
        this.disposable.add(
                Observable.interval(1, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(Schedulers.io())
                        .subscribe(t -> ratesRepository.requestRatesUpdate(), Throwable::printStackTrace)
        );
    }

    @Override
    public void attachView(RatesContract.view view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void pause() {
        disposable.clear();
    }

    @Override
    public void resume() {
        this.disposable.add(
                ratesRepository.getRates()
                .subscribeOn(Schedulers.computation())
                        //adding debounce to prevent many quick updates due to network lag
                .debounce(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleRatesUpdate)
        );

        requestRates();
    }

    private void handleRatesUpdate(RatesData ratesData) {
        view.updateRates(ratesData.getRates());
    }
}
