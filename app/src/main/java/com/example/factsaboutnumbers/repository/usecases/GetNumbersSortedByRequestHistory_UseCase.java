package com.example.factsaboutnumbers.repository.usecases;

import com.example.factsaboutnumbers.model.Number;
import com.example.factsaboutnumbers.repository.NumbersRepository;

import java.util.List;

import io.reactivex.Flowable;
import kotlinx.coroutines.flow.Flow;

import javax.inject.Inject;

public class GetNumbersSortedByRequestHistory_UseCase {

    private NumbersRepository numbersRepository;

    @Inject
    GetNumbersSortedByRequestHistory_UseCase(NumbersRepository numbersRepository) {
        this.numbersRepository = numbersRepository;
    }

    public Flowable<List<Number>> execute() {
        return numbersRepository.getNumbersSortedByRequestHistory();
    }
}