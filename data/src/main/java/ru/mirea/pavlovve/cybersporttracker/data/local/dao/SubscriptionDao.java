package ru.mirea.pavlovve.cybersporttracker.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.data.local.entity.SubscriptionEntity;

@Dao
public interface SubscriptionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SubscriptionEntity entity);

    @Query("SELECT * FROM subscriptions")
    List<SubscriptionEntity> getAll();

    @Query("DELETE FROM subscriptions WHERE tournamentId = :tournamentId")
    void deleteById(int tournamentId);
}
