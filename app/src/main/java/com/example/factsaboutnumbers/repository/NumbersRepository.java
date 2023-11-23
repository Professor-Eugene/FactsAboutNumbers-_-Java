package com.example.factsaboutnumbers.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.factsaboutnumbers.L;
import com.example.factsaboutnumbers.data.rest.NumbersApiService;
import com.example.factsaboutnumbers.data.room.NumbersDao;
import com.example.factsaboutnumbers.di.util.AppScope;
import com.example.factsaboutnumbers.model.Number;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

@AppScope
public class NumbersRepository {

    private NumbersApiService numbersApiService;

    private NumbersDao numbersDao;

    private Flowable<List<Number>> numbersSortedByRequestHistory;

    private Flowable<List<Number>> numbersSortedAscending;

    @Inject
    NumbersRepository(NumbersApiService numbersApiService, NumbersDao numbersDao) {
        this.numbersApiService = numbersApiService;
        this.numbersDao = numbersDao;
        numbersSortedByRequestHistory = numbersDao.getNumbersSortedByRequestHistory();
        numbersSortedAscending = numbersDao.getNumbersSortedAscending();
    }

    public Flowable<List<Number>> getNumbersSortedByRequestHistory() {
        return numbersSortedByRequestHistory;
    }

    public Flowable<List<Number>> getNumbersSortedAscending() {
        return numbersSortedAscending;
    }

    public Single<String> requestNewFact(int number) {
        return numbersApiService.requestNewFact(number);
    }

    public Single<Number> getNumberSelectedFromList(int number) {
        return numbersDao.getNumberSelectedFromList(number);
    }

    public long insertNumber(Number number) {
        return numbersDao.insert(number);
    }
}
