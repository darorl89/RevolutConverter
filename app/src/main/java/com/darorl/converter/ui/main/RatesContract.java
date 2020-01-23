package com.darorl.converter.ui.main;

import com.darorl.converter.ui.BasePresenter;

import java.math.BigDecimal;
import java.util.Map;

public interface RatesContract {
    interface view {
        void updateRates(Map<String, BigDecimal> map);
    }
    interface presenter<V> extends BasePresenter<V> {
        void requestRates();
    }
}
