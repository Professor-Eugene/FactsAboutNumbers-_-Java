package com.example.factsaboutnumbers.repository.usecases;

import com.example.factsaboutnumbers.model.Number;
import com.example.factsaboutnumbers.repository.NumbersRepository;
import javax.inject.Inject;

public class SaveNumberToDatabase_UseCase {

    private NumbersRepository numbersRepository;

    @Inject
    SaveNumberToDatabase_UseCase(NumbersRepository numbersRepository) {
        this.numbersRepository = numbersRepository;
    }

    public long execute(Number number) {
        return numbersRepository.insertNumber(number);
    }

}