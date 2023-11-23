package com.example.factsaboutnumbers.data.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.factsaboutnumbers.model.Number;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface NumbersDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Number number);

    @Query("SELECT * from numbers_table ORDER BY id DESC")
    Flowable<List<Number>> getNumbersSortedByRequestHistory();

    @Query("SELECT * from numbers_table ORDER BY number ASC")
    Flowable<List<Number>> getNumbersSortedAscending();

    @Query("SELECT * from numbers_table WHERE id = :id")
    Single<Number> getNumberSelectedFromList(int id);
}
