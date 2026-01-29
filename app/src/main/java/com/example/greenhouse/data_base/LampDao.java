package com.example.greenhouse.data_base;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LampDao {

    @Insert
    void insert (LampEntity lamp);

    @Update
    void update (LampEntity lamp);
    @Update
    void updateAll(List<LampEntity> lamps);

    @Query("SELECT * FROM lamps_table WHERE shelfId = :shelfId ORDER BY lampNumber ASC")
    LiveData<List<LampEntity>> getLampsByShelf(int shelfId);

    @Query("SELECT COUNT(*) FROM lamps_table")
    int getCount();


}
