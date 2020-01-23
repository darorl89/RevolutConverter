package com.darorl.converter.ui;

import android.view.View;

public interface BasePresenter<V> {

    void attachView(V v);

    void detachView();

    default void pause() {}

    default void resume() {}
}
