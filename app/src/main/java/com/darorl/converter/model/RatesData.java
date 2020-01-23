package com.darorl.converter.model;

import java.math.BigDecimal;
import java.util.HashMap;

public class RatesData {
    private String base;
    private String date;
    private HashMap<String, BigDecimal> rates;

    public RatesData(String base, String date, HashMap<String, BigDecimal> rates) {
        this.base = base;
        this.date = date;
        this.rates = rates;
    }

    public String getBase() {
        return base;
    }

    public String getDate() {
        return date;
    }

    public HashMap<String, BigDecimal> getRates() {
        return rates;
    }
}
