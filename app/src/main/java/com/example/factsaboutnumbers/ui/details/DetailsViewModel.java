package com.example.factsaboutnumbers.ui.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.factsaboutnumbers.L;
import com.example.factsaboutnumbers.R;
import com.example.factsaboutnumbers.model.Number;
import com.example.factsaboutnumbers.repository.usecases.GetNumberSelectedFromList_UseCase;
import com.example.factsaboutnumbers.repository.usecases.RequestNewFact_UseCase;
import com.example.factsaboutnumbers.repository.usecases.SaveNumberToDatabase_UseCase;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class DetailsViewModel extends ViewModel {

    SaveNumberToDatabase_UseCase saveNumberToDatabase_UseCase;

    RequestNewFact_UseCase requestNewFact_UseCase;

    GetNumberSelectedFromList_UseCase getNumberSelectedFromList_UseCase;

    private MutableLiveData<Number> numberLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private MutableLiveData<Boolean> numbersLoadError = new MutableLiveData<>();

    private MutableLiveData<Integer> toast = new MutableLiveData<>();

    LiveData<Number> getNumberLiveData() {
        return numberLiveData;
    }

    LiveData<Boolean> getLoading() {
        return loading;
    }

    LiveData<Boolean> getError() {
        return numbersLoadError;
    }

    LiveData<Integer> getToast() {
        return toast;
    }

    private CompositeDisposable disposable = new CompositeDisposable();


    @Inject
    DetailsViewModel(SaveNumberToDatabase_UseCase saveNumberToDatabase_UseCase,
                     RequestNewFact_UseCase requestNewFact_UseCase,
                     GetNumberSelectedFromList_UseCase getNumberSelectedFromList_UseCase) {

        this.saveNumberToDatabase_UseCase = saveNumberToDatabase_UseCase;
        this.requestNewFact_UseCase = requestNewFact_UseCase;
        this.getNumberSelectedFromList_UseCase = getNumberSelectedFromList_UseCase;
    }

    public void requestNewFact(int intNumberRequested) {
        hideErrorMessage();
        showLoadingProgressBar();

        disposable.add(requestNewFact_UseCase.execute(intNumberRequested)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<String>() {
                    @Override
                    public void onSuccess(String fact) {
                        hideLoadingProgressBar();
                        Number number = new Number(intNumberRequested, fact);
                        showNumber(number);
                        saveNumberToDatabase(number);
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoadingProgressBar();
                        showErrorMessage();
                    }
                }));
    }

    public void getSelectedFromList(int number) {
        hideErrorMessage();

        SingleObserver<Number> singleObserver = getNumberSelectedFromList_UseCase.execute(number)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Number>() {
                    @Override
                    public void onSuccess(Number number) {
                        showNumber(number);
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.og("Error " + e);
                    }
                });
    }

    void saveNumberToDatabase(Number number) {
        Observable.fromCallable(() -> saveNumberToDatabase_UseCase.execute(number))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long insertIndex) {
                        if (insertIndex == -1) {
                            showToast(R.string.fact_is_present);                 }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    void showLoadingProgressBar() {
        loading.setValue(true);
    }

    void hideLoadingProgressBar() {
        loading.setValue(false);
    }

    void showErrorMessage() {
        numbersLoadError.setValue(true);
    }

    void hideErrorMessage() {
        numbersLoadError.setValue(false);
    }

    void showNumber(Number number) {
        numberLiveData.setValue(number);
    }

    void showToast(int resource) {
        toast.setValue(resource);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null) {
            disposable.clear();
            disposable = null;
        }
    }
}
