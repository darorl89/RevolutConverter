package com.darorl.converter.api;

import com.darorl.converter.model.RatesData;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RatesService {

    @GET("latest?base=EUR")
    Call<RatesData> getRates();
}
