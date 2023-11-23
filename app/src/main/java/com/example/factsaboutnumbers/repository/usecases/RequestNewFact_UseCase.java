package com.example.factsaboutnumbers.repository.usecases;

import com.example.factsaboutnumbers.repository.NumbersRepository;

import io.reactivex.Single;
import retrofit2.Response;
import javax.inject.Inject;

public class RequestNewFact_UseCase {

    private NumbersRepository numbersRepository;

    @Inject
    RequestNewFact_UseCase(NumbersRepository numbersRepository) {
        this.numbersRepository = numbersRepository;
    }

    public Single<String> execute(int number) {
        return numbersRepository.requestNewFact(number);
    }

}