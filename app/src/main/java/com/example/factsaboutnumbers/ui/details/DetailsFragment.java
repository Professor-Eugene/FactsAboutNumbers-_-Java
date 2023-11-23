package com.example.factsaboutnumbers.ui.details;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.factsaboutnumbers.L;
import com.example.factsaboutnumbers.R;
import com.example.factsaboutnumbers.base.BaseFragment;
import com.example.factsaboutnumbers.model.Number;
import com.example.factsaboutnumbers.util.Constants;

import javax.inject.Inject;

import butterknife.BindView;

public class DetailsFragment extends BaseFragment {
    private final static String ARG_NUMBER = "number";
    private final static String ARG_METHOD = "method";

    @BindView(R.id.details_number)
    TextView numberTextView;

    @BindView(R.id.details_text)
    TextView factTextView;

    @BindView(R.id.details_loading_view)
    View loadingView;

    @BindView(R.id.details_tv_error)
    TextView errorTextView;

    ActionBar actionBar;

    public static DetailsFragment newInstance(int number, String method) {
        DetailsFragment fragment = new DetailsFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt(ARG_NUMBER, number);
        args.putString(ARG_METHOD, method);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_details;
    }

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private DetailsViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        viewModel = new ViewModelProvider(this, viewModelFactory).get(DetailsViewModel.class);

        int number = getArguments().getInt(ARG_NUMBER);
        String method = getArguments().getString(ARG_METHOD);

        if (method == Constants.REQUEST) {
            viewModel.requestNewFact(number);
        } else if (method == Constants.SELECT) {
            viewModel.getSelectedFromList(number);
        }

        observeViewModel();
    }

    void observeViewModel() {

        viewModel.getNumberLiveData().observe(getViewLifecycleOwner(), number -> {
            String stringNumber = String.valueOf(number.getNumber());
            numberTextView.setText(stringNumber);
            String rawFact = number.getFact();
            String fact = rawFact.replaceFirst(stringNumber + " ", "");
            factTextView.setText(fact);
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                if (isLoading) {
                    numberTextView.setText("");
                    factTextView.setText("");
                    loadingView.setVisibility(View.VISIBLE);
                } else {
                    loadingView.setVisibility(View.GONE);
                }
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), isError -> {
            if (isError != null)
                if (isError) {
                    errorTextView.setVisibility(View.VISIBLE);
                    errorTextView.setText("An Error Occurred While Loading Data!");
                } else {
                    errorTextView.setVisibility(View.GONE);
                    errorTextView.setText(null);
                }
        });

        viewModel.getToast().observe(getViewLifecycleOwner(), resource -> {
            Toast.makeText(getActivity(), resource, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                actionBar.setDisplayHomeAsUpEnabled(false);
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
