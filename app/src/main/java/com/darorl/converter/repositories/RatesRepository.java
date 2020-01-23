package com.darorl.converter.repositories;

import com.darorl.converter.model.RatesData;

import io.reactivex.Observable;

public interface RatesRepository {
    Observable<RatesData> getRates();
    void requestRatesUpdate();
}
