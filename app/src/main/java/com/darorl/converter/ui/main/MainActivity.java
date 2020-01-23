package com.darorl.converter.ui.main;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.darorl.converter.App;
import com.darorl.converter.R;
import com.darorl.converter.adapters.CurrenciesAdapter;

import java.math.BigDecimal;
import java.util.Map;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RatesContract.view {

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Inject
    RatesContract.presenter<RatesContract.view> presenter;

    private CurrenciesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        App.getAppComponent().inject(this);
        presenter.attachView(this);
        initRecycler();
    }

    private void initRecycler() {
        adapter = new CurrenciesAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void updateRates(Map<String, BigDecimal> map) {
        if (progressBar.getVisibility() != View.GONE) {
            progressBar.setVisibility(View.GONE);
        }
        adapter.updateRates(map);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}