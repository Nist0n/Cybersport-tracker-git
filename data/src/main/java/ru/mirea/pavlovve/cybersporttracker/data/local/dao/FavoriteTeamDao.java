package ru.mirea.pavlovve.cybersporttracker.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.data.local.entity.FavoriteTeamEntity;

@Dao
public interface FavoriteTeamDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavoriteTeamEntity entity);

    @Query("SELECT * FROM favorite_teams")
    List<FavoriteTeamEntity> getAll();

    @Query("DELETE FROM favorite_teams WHERE id = :teamId")
    void deleteById(int teamId);
}
