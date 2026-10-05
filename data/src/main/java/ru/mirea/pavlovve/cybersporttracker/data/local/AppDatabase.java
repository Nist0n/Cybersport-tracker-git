package ru.mirea.pavlovve.cybersporttracker.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ru.mirea.pavlovve.cybersporttracker.data.local.dao.FavoriteTeamDao;
import ru.mirea.pavlovve.cybersporttracker.data.local.dao.PredictionDao;
import ru.mirea.pavlovve.cybersporttracker.data.local.dao.SubscriptionDao;
import ru.mirea.pavlovve.cybersporttracker.data.local.entity.FavoriteTeamEntity;
import ru.mirea.pavlovve.cybersporttracker.data.local.entity.PredictionEntity;
import ru.mirea.pavlovve.cybersporttracker.data.local.entity.SubscriptionEntity;

@Database(entities = {
        FavoriteTeamEntity.class,
        PredictionEntity.class,
        SubscriptionEntity.class
}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "cybersport_tracker.db";
    private static volatile AppDatabase instance;

    public abstract FavoriteTeamDao favoriteTeamDao();

    public abstract PredictionDao predictionDao();

    public abstract SubscriptionDao subscriptionDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    DB_NAME)

                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return instance;
    }
}
