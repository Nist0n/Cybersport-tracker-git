package ru.mirea.pavlovve.cybersporttracker.data.local.dao;

import androidx.annotation.NonNull;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import ru.mirea.pavlovve.cybersporttracker.data.local.entity.SubscriptionEntity;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class SubscriptionDao_Impl implements SubscriptionDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<SubscriptionEntity> __insertAdapterOfSubscriptionEntity;

  public SubscriptionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfSubscriptionEntity = new EntityInsertAdapter<SubscriptionEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `subscriptions` (`tournamentId`,`name`,`game`,`location`,`startDate`,`prizePool`,`status`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          final SubscriptionEntity entity) {
        statement.bindLong(1, entity.getTournamentId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getName());
        }
        if (entity.getGame() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getGame());
        }
        if (entity.getLocation() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getLocation());
        }
        if (entity.getStartDate() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getStartDate());
        }
        if (entity.getPrizePool() == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.getPrizePool());
        }
        if (entity.getStatus() == null) {
          statement.bindNull(7);
        } else {
          statement.bindText(7, entity.getStatus());
        }
      }
    };
  }

  @Override
  public void insert(final SubscriptionEntity entity) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfSubscriptionEntity.insert(_connection, entity);
      return null;
    });
  }

  @Override
  public List<SubscriptionEntity> getAll() {
    final String _sql = "SELECT * FROM subscriptions";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfTournamentId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tournamentId");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfGame = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "game");
        final int _columnIndexOfLocation = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "location");
        final int _columnIndexOfStartDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startDate");
        final int _columnIndexOfPrizePool = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "prizePool");
        final int _columnIndexOfStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "status");
        final List<SubscriptionEntity> _result = new ArrayList<SubscriptionEntity>();
        while (_stmt.step()) {
          final SubscriptionEntity _item;
          final int _tmpTournamentId;
          _tmpTournamentId = (int) (_stmt.getLong(_columnIndexOfTournamentId));
          final String _tmpName;
          if (_stmt.isNull(_columnIndexOfName)) {
            _tmpName = null;
          } else {
            _tmpName = _stmt.getText(_columnIndexOfName);
          }
          final String _tmpGame;
          if (_stmt.isNull(_columnIndexOfGame)) {
            _tmpGame = null;
          } else {
            _tmpGame = _stmt.getText(_columnIndexOfGame);
          }
          final String _tmpLocation;
          if (_stmt.isNull(_columnIndexOfLocation)) {
            _tmpLocation = null;
          } else {
            _tmpLocation = _stmt.getText(_columnIndexOfLocation);
          }
          final String _tmpStartDate;
          if (_stmt.isNull(_columnIndexOfStartDate)) {
            _tmpStartDate = null;
          } else {
            _tmpStartDate = _stmt.getText(_columnIndexOfStartDate);
          }
          final String _tmpPrizePool;
          if (_stmt.isNull(_columnIndexOfPrizePool)) {
            _tmpPrizePool = null;
          } else {
            _tmpPrizePool = _stmt.getText(_columnIndexOfPrizePool);
          }
          final String _tmpStatus;
          if (_stmt.isNull(_columnIndexOfStatus)) {
            _tmpStatus = null;
          } else {
            _tmpStatus = _stmt.getText(_columnIndexOfStatus);
          }
          _item = new SubscriptionEntity(_tmpTournamentId,_tmpName,_tmpGame,_tmpLocation,_tmpStartDate,_tmpPrizePool,_tmpStatus);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void deleteById(final int tournamentId) {
    final String _sql = "DELETE FROM subscriptions WHERE tournamentId = ?";
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, tournamentId);
        _stmt.step();
        return null;
      } finally {
        _stmt.close();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
