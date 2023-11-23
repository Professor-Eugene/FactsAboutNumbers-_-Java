package com.example.factsaboutnumbers.repository.usecases;

import com.example.factsaboutnumbers.model.Number;
import com.example.factsaboutnumbers.repository.NumbersRepository;
import javax.inject.Inject;

import io.reactivex.Single;

public class GetNumberSelectedFromList_UseCase {

    private NumbersRepository numbersRepository;

    @Inject
    GetNumberSelectedFromList_UseCase(NumbersRepository numbersRepository) {
        this.numbersRepository = numbersRepository;
    }

    public Single<Number> execute(int id) {
        return numbersRepository.getNumberSelectedFromList(id);
    }

}