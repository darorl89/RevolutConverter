package com.darorl.converter.repositories;

import com.darorl.converter.api.RatesService;
import com.darorl.converter.model.RatesData;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatesRepositoryImpl implements RatesRepository {

    private RatesService service;

    private BehaviorSubject<RatesData> ratesSubject;

    public RatesRepositoryImpl(RatesService service) {
        this.service = service;
        ratesSubject = BehaviorSubject.create();
    }

    public Observable<RatesData> getRates() {
        return ratesSubject.hide();
    }

    public void requestRatesUpdate() {
        service.getRates().enqueue(new Callback<RatesData>() {
            @Override
            public void onResponse(Call<RatesData> call, Response<RatesData> response) {
                if (response.code() == 200 && response.body() != null) {
                    ratesSubject.onNext(response.body());
                }
            }

            @Override
            public void onFailure(Call<RatesData> call, Throwable t) {

            }
        });
    }
}
