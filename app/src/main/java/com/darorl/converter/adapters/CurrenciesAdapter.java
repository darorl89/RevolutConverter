package com.darorl.converter.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.darorl.converter.R;
import com.darorl.converter.util.CircleTransform;
import com.darorl.converter.util.Constants;
import com.darorl.converter.util.Utilities;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrenciesAdapter extends RecyclerView.Adapter<CurrenciesAdapter.ViewHolder> {

    private List<String> symbols;
    private Map<String, BigDecimal> ratesMap;

    private BigDecimal eurAmount;
    private String baseCurrency;

    private boolean initialized = false;

    public CurrenciesAdapter() {
        this.symbols = new LinkedList<>();
        this.ratesMap = new HashMap<>();
        this.baseCurrency = "EUR";
        this.eurAmount = new BigDecimal(100);
    }

    private void init(Map<String, BigDecimal> map) {
        this.symbols.addAll(map.keySet());
        Collections.sort(this.symbols);
        this.ratesMap.put("EUR", BigDecimal.ONE);
        this.symbols.add(0, baseCurrency);
        initialized = true;
    }

    public void updateRates(Map<String, BigDecimal> map) {
        if (!initialized) {
            init(map);
            this.ratesMap.putAll(map);
            notifyDataSetChanged();
        } else {
            this.ratesMap.putAll(map);
            notifyItemRangeChanged(0, symbols.size(), Integer.valueOf(1));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View row = inflater.inflate(R.layout.recycler_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(context, row);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String symbol = symbols.get(position);

        holder.symbolTextView.setText(symbol);

        int resId = Utilities.getResId(String.format("currency_%s", symbol), R.string.class);
        if (resId >= 0) {
            holder.captionTextView.setText(holder.context.getText(resId));
        }

        String url = String.format(Constants.FLAGS_URL, symbol.toLowerCase());

        Picasso.get()
                .load(url)
                .transform(new CircleTransform())
                .placeholder(R.drawable.image_placeholder)
                .into(holder.imageView);

        holder.updateValue();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        holder.itemView.setTag(position);
        if (payloads == null || payloads.size() == 0) {
            onBindViewHolder(holder, position);
        } else {
            holder.updateValue();
        }
    }

    @Override
    public int getItemCount() {
        return this.symbols.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView)
        ImageView imageView;

        @BindView(R.id.symbolTextView)
        TextView symbolTextView;

        @BindView(R.id.captionTextView)
        TextView captionTextView;

        @BindView(R.id.amountEditText)
        EditText amountEditText;

        private Context context;

        private boolean hasFocus = false;

        private void updateBaseCurrencyValue() {
            String symbol = symbolTextView.getText().toString();
            baseCurrency = symbol;
            BigDecimal rate = ratesMap.get(symbol);
            BigDecimal value;
            try {
                value = new BigDecimal(amountEditText.getText().toString());
            } catch (Exception ex) {
                value = BigDecimal.ZERO;
            }
            if (rate.equals(BigDecimal.ZERO) || value.equals(BigDecimal.ZERO)) {
                eurAmount = BigDecimal.ZERO;
            } else {
                eurAmount = value.divide(rate, 2, RoundingMode.HALF_EVEN);
            }

            CurrenciesAdapter.this.notifyItemRangeChanged(0, symbols.size(), 1);
        }

        void updateValue() {
            if (!this.hasFocus) {
                String symbol = symbolTextView.getText().toString();
                BigDecimal rate = ratesMap.get(symbol);
                if (rate == null || eurAmount == null) {
                    return;
                }
                BigDecimal amount = eurAmount.multiply(rate).setScale(2, BigDecimal.ROUND_DOWN).stripTrailingZeros();

                amountEditText.setText(amount.toPlainString());
            }
        }

        private View.OnFocusChangeListener focusLlistener = (v, hasFocus) -> {
            this.hasFocus = hasFocus;
            if (hasFocus) {
                updateBaseCurrencyValue();
                reorderToTop();
            }
        };

        private TextWatcher textListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (hasFocus && s.length() == 0) {
                    amountEditText.removeTextChangedListener(textListener);
                    amountEditText.setText("0");
                    amountEditText.addTextChangedListener(textListener);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (hasFocus) {
                    updateBaseCurrencyValue();
                }
            }
        };

        private void reorderToTop() {
            Object pos = itemView.getTag();
            if (pos != null) {
                int position = (int) pos;
                String symbol = symbols.get(position);
                if (position > 0) {
                    symbols.remove(position);
                    symbols.add(0, symbol);
                    notifyItemMoved(position, 0);
                }

                InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (in != null) {
                    in.showSoftInput(amountEditText, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        }

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            ButterKnife.bind(this, itemView);
            amountEditText.setOnFocusChangeListener(focusLlistener);
            amountEditText.addTextChangedListener(textListener);
            itemView.setClickable(true);
            itemView.setOnClickListener(v -> amountEditText.requestFocus());
        }
    }
}


