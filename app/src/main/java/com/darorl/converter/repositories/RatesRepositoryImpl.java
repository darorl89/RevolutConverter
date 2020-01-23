package com.darorl.converter.repositories;

import com.darorl.converter.api.RatesService;
import com.darorl.converter.model.RatesData;
import com.darorl.converter.util.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RatesRepositoryImpl implements RatesRepository {

    private RatesService service;

    private BehaviorSubject<RatesData> ratesSubject;

    public RatesRepositoryImpl() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        service = retrofit.create(RatesService.class);

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
