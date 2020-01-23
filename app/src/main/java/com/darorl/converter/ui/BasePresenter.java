package com.darorl.converter.ui;

public interface BasePresenter<V> {

    void attachView(V v);

    void detachView();

    default void pause() {}

    default void resume() {}
}
