package com.example.factsaboutnumbers.di.module;

import android.content.Context;

import androidx.room.Room;

import com.example.factsaboutnumbers.data.room.NumbersDao;
import com.example.factsaboutnumbers.di.util.AppScope;
import com.example.factsaboutnumbers.data.room.NumbersRoomDatabase;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomModule {

    private static final String DATABASE_NAME = "numbers_database";

    @AppScope
    @Provides
    static NumbersRoomDatabase provideNumbersRoomDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), NumbersRoomDatabase.class, DATABASE_NAME).build();
//        return NumbersRoomDatabase.getDatabase(context);
    }

    @AppScope
    @Provides
    static NumbersDao provideNumbersDao(NumbersRoomDatabase numbersRoomDatabase) {
        return numbersRoomDatabase.numbersDao();
    }
}
