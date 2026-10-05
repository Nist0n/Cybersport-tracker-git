package ru.mirea.pavlovve.cybersporttracker.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.data.local.entity.PredictionEntity;

@Dao
public interface PredictionDao {

    @Insert
    long insert(PredictionEntity entity);

    @Query("SELECT * FROM predictions ORDER BY id DESC")
    List<PredictionEntity> getAll();

    @Query("DELETE FROM predictions")
    void deleteAll();
}
