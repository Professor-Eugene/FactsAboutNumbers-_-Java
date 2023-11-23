package com.example.factsaboutnumbers.ui.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.factsaboutnumbers.R;
import com.example.factsaboutnumbers.model.Number;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NumbersListAdapter extends RecyclerView.Adapter<NumbersListAdapter.NumberViewHolder> {

    private NumberSelectedListener numberSelectedListener;
    private List<Number> data = new ArrayList<>();

    NumbersListAdapter(LifecycleOwner lifecycleOwner, LiveData<List<Number>> numbersLiveData, NumberSelectedListener numberSelectedListener) {
        this.numberSelectedListener = numberSelectedListener;
        setHasStableIds(true);

        numbersLiveData.observe(lifecycleOwner, numbers -> {
            data = numbers;
            notifyDataSetChanged();
        });
    }

    @NonNull
    @Override
    public NumberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.numbers_list_item, parent, false);
        return new NumberViewHolder(view, numberSelectedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NumberViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    static final class NumberViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_number)
        TextView numberTextView;

        @BindView(R.id.tv_fact)
        TextView factTextView;

        private Number number;

        NumberViewHolder(View itemView, NumberSelectedListener numberSelectedListener) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> {
                if (number != null) {
                    numberSelectedListener.onNumberSelected(number.getId());
                }
            });
        }

        void bind(Number numberObject) {
            this.number = numberObject;

            String stringNumberValue = String.valueOf(numberObject.getNumber());
            numberTextView.setText(stringNumberValue);

            String rawFact = number.getFact();
            String fact = reformatFact(rawFact, stringNumberValue);

            factTextView.setText(fact);
        }

        String reformatFact(String rawFact, String stringValueOfNumber) {
            String fact = rawFact.replaceFirst(stringValueOfNumber + " ", "");
            return fact;
        }
    }
}