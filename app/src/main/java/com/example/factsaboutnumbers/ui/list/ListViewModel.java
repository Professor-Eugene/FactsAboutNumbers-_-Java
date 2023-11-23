package com.example.factsaboutnumbers.ui.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.factsaboutnumbers.model.Number;
import com.example.factsaboutnumbers.repository.NumbersRepository;
import com.example.factsaboutnumbers.repository.usecases.GetNumbersSortedAscending_UseCase;
import com.example.factsaboutnumbers.repository.usecases.GetNumbersSortedByRequestHistory_UseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public class ListViewModel extends ViewModel {

    GetNumbersSortedAscending_UseCase getNumbersSortedAscending_Usecase;

    GetNumbersSortedByRequestHistory_UseCase getNumbersSortedByRequestHistory_Usecase;

    private MutableLiveData<List<Number>> numbersLiveData = new MutableLiveData<>();

    public LiveData<List<Number>> getNumbersLiveData() {
        return numbersLiveData;
    }

    private final MutableLiveData<LiveData<List<Number>>> updateListLiveData = new MutableLiveData<>();

    public LiveData<LiveData<List<Number>>> getUpdateListLiveData() {
        return updateListLiveData;
    }

    private boolean sortByRequestHistory = true;

    private Disposable disposable;

    @Inject
    ListViewModel(GetNumbersSortedAscending_UseCase getNumbersSortedAscending_Usecase,
    GetNumbersSortedByRequestHistory_UseCase getNumbersSortedByRequestHistory_Usecase) {
        this.getNumbersSortedAscending_Usecase = getNumbersSortedAscending_Usecase;
        this.getNumbersSortedByRequestHistory_Usecase = getNumbersSortedByRequestHistory_Usecase;
        selectDataSource();
    }

    private void selectDataSource() {
        if (disposable != null)
            disposable.dispose();

        if (sortByRequestHistory) {
            disposable = getNumbersSortedByRequestHistory_Usecase.execute()
                    .subscribe(numbers -> numbersLiveData.postValue(numbers));
        } else {
            disposable = getNumbersSortedAscending_Usecase.execute()
                    .subscribe(numbers -> numbersLiveData.postValue(numbers));
        }
    }

    public void toggleSortOrder() {
        sortByRequestHistory = !sortByRequestHistory;
        selectDataSource();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null)
            disposable.dispose();
    }


//    private void initNumbersLiveData() {
//        if (sortByRequestHistory) {
//            numbersLiveData = LiveDataReactiveStreams.fromPublisher(
//                    numbersRepository.getNumbersSortedByRequestHistory()
//            );
//        } else {
//            numbersLiveData = LiveDataReactiveStreams.fromPublisher(
//                    numbersRepository.getNumbersSortedAscending()
//            );
//        }
//
//        updateListLiveData.setValue(numbersLiveData);
//    }
}
