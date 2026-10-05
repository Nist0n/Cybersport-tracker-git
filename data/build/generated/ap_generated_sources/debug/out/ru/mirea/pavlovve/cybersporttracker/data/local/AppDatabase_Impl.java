package ru.mirea.pavlovve.cybersporttracker.data.local;

import androidx.annotation.NonNull;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenDelegate;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import ru.mirea.pavlovve.cybersporttracker.data.local.dao.FavoriteTeamDao;
import ru.mirea.pavlovve.cybersporttracker.data.local.dao.FavoriteTeamDao_Impl;
import ru.mirea.pavlovve.cybersporttracker.data.local.dao.PredictionDao;
import ru.mirea.pavlovve.cybersporttracker.data.local.dao.PredictionDao_Impl;
import ru.mirea.pavlovve.cybersporttracker.data.local.dao.SubscriptionDao;
import ru.mirea.pavlovve.cybersporttracker.data.local.dao.SubscriptionDao_Impl;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile FavoriteTeamDao _favoriteTeamDao;

  private volatile PredictionDao _predictionDao;

  private volatile SubscriptionDao _subscriptionDao;

  @Override
  @NonNull
  protected RoomOpenDelegate createOpenDelegate() {
    final RoomOpenDelegate _openDelegate = new RoomOpenDelegate(1, "d08d24c194f9c5b15a92c8bec20490fe", "f18b32c03b1dbf09c768d654b20ae647") {
      @Override
      public void createAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `favorite_teams` (`id` INTEGER NOT NULL, `tag` TEXT, `name` TEXT, `country` TEXT, `logoUrl` TEXT, `worldRank` INTEGER NOT NULL, `players` TEXT, PRIMARY KEY(`id`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `predictions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `matchId` INTEGER NOT NULL, `teamTag` TEXT, `score` TEXT, `coefficient` REAL NOT NULL, `status` TEXT)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `subscriptions` (`tournamentId` INTEGER NOT NULL, `name` TEXT, `game` TEXT, `location` TEXT, `startDate` TEXT, `prizePool` TEXT, `status` TEXT, PRIMARY KEY(`tournamentId`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        SQLite.execSQL(connection, "INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'd08d24c194f9c5b15a92c8bec20490fe')");
      }

      @Override
      public void dropAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `favorite_teams`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `predictions`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `subscriptions`");
      }

      @Override
      public void onCreate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      public void onOpen(@NonNull final SQLiteConnection connection) {
        internalInitInvalidationTracker(connection);
      }

      @Override
      public void onPreMigrate(@NonNull final SQLiteConnection connection) {
        DBUtil.dropFtsSyncTriggers(connection);
      }

      @Override
      public void onPostMigrate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      @NonNull
      public RoomOpenDelegate.ValidationResult onValidateSchema(
          @NonNull final SQLiteConnection connection) {
        final Map<String, TableInfo.Column> _columnsFavoriteTeams = new HashMap<String, TableInfo.Column>(7);
        _columnsFavoriteTeams.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteTeams.put("tag", new TableInfo.Column("tag", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteTeams.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteTeams.put("country", new TableInfo.Column("country", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteTeams.put("logoUrl", new TableInfo.Column("logoUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteTeams.put("worldRank", new TableInfo.Column("worldRank", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteTeams.put("players", new TableInfo.Column("players", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysFavoriteTeams = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesFavoriteTeams = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFavoriteTeams = new TableInfo("favorite_teams", _columnsFavoriteTeams, _foreignKeysFavoriteTeams, _indicesFavoriteTeams);
        final TableInfo _existingFavoriteTeams = TableInfo.read(connection, "favorite_teams");
        if (!_infoFavoriteTeams.equals(_existingFavoriteTeams)) {
          return new RoomOpenDelegate.ValidationResult(false, "favorite_teams(ru.mirea.pavlovve.cybersporttracker.data.local.entity.FavoriteTeamEntity).\n"
                  + " Expected:\n" + _infoFavoriteTeams + "\n"
                  + " Found:\n" + _existingFavoriteTeams);
        }
        final Map<String, TableInfo.Column> _columnsPredictions = new HashMap<String, TableInfo.Column>(6);
        _columnsPredictions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPredictions.put("matchId", new TableInfo.Column("matchId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPredictions.put("teamTag", new TableInfo.Column("teamTag", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPredictions.put("score", new TableInfo.Column("score", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPredictions.put("coefficient", new TableInfo.Column("coefficient", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPredictions.put("status", new TableInfo.Column("status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysPredictions = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesPredictions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPredictions = new TableInfo("predictions", _columnsPredictions, _foreignKeysPredictions, _indicesPredictions);
        final TableInfo _existingPredictions = TableInfo.read(connection, "predictions");
        if (!_infoPredictions.equals(_existingPredictions)) {
          return new RoomOpenDelegate.ValidationResult(false, "predictions(ru.mirea.pavlovve.cybersporttracker.data.local.entity.PredictionEntity).\n"
                  + " Expected:\n" + _infoPredictions + "\n"
                  + " Found:\n" + _existingPredictions);
        }
        final Map<String, TableInfo.Column> _columnsSubscriptions = new HashMap<String, TableInfo.Column>(7);
        _columnsSubscriptions.put("tournamentId", new TableInfo.Column("tournamentId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubscriptions.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubscriptions.put("game", new TableInfo.Column("game", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubscriptions.put("location", new TableInfo.Column("location", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubscriptions.put("startDate", new TableInfo.Column("startDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubscriptions.put("prizePool", new TableInfo.Column("prizePool", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubscriptions.put("status", new TableInfo.Column("status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysSubscriptions = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesSubscriptions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSubscriptions = new TableInfo("subscriptions", _columnsSubscriptions, _foreignKeysSubscriptions, _indicesSubscriptions);
        final TableInfo _existingSubscriptions = TableInfo.read(connection, "subscriptions");
        if (!_infoSubscriptions.equals(_existingSubscriptions)) {
          return new RoomOpenDelegate.ValidationResult(false, "subscriptions(ru.mirea.pavlovve.cybersporttracker.data.local.entity.SubscriptionEntity).\n"
                  + " Expected:\n" + _infoSubscriptions + "\n"
                  + " Found:\n" + _existingSubscriptions);
        }
        return new RoomOpenDelegate.ValidationResult(true, null);
      }
    };
    return _openDelegate;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final Map<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final Map<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "favorite_teams", "predictions", "subscriptions");
  }

  @Override
  public void clearAllTables() {
    super.performClear(false, "favorite_teams", "predictions", "subscriptions");
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final Map<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(FavoriteTeamDao.class, FavoriteTeamDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PredictionDao.class, PredictionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SubscriptionDao.class, SubscriptionDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final Set<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public FavoriteTeamDao favoriteTeamDao() {
    if (_favoriteTeamDao != null) {
      return _favoriteTeamDao;
    } else {
      synchronized(this) {
        if(_favoriteTeamDao == null) {
          _favoriteTeamDao = new FavoriteTeamDao_Impl(this);
        }
        return _favoriteTeamDao;
      }
    }
  }

  @Override
  public PredictionDao predictionDao() {
    if (_predictionDao != null) {
      return _predictionDao;
    } else {
      synchronized(this) {
        if(_predictionDao == null) {
          _predictionDao = new PredictionDao_Impl(this);
        }
        return _predictionDao;
      }
    }
  }

  @Override
  public SubscriptionDao subscriptionDao() {
    if (_subscriptionDao != null) {
      return _subscriptionDao;
    } else {
      synchronized(this) {
        if(_subscriptionDao == null) {
          _subscriptionDao = new SubscriptionDao_Impl(this);
        }
        return _subscriptionDao;
      }
    }
  }
}
