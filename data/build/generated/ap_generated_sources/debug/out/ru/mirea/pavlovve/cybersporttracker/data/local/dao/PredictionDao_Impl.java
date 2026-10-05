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
import ru.mirea.pavlovve.cybersporttracker.data.local.entity.PredictionEntity;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class PredictionDao_Impl implements PredictionDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<PredictionEntity> __insertAdapterOfPredictionEntity;

  public PredictionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfPredictionEntity = new EntityInsertAdapter<PredictionEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `predictions` (`id`,`matchId`,`teamTag`,`score`,`coefficient`,`status`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final PredictionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getMatchId());
        if (entity.getTeamTag() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getTeamTag());
        }
        if (entity.getScore() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getScore());
        }
        statement.bindDouble(5, entity.getCoefficient());
        if (entity.getStatus() == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.getStatus());
        }
      }
    };
  }

  @Override
  public long insert(final PredictionEntity entity) {
    return DBUtil.performBlocking(__db, false, true, (_connection) -> {
      return __insertAdapterOfPredictionEntity.insertAndReturnId(_connection, entity);
    });
  }

  @Override
  public List<PredictionEntity> getAll() {
    final String _sql = "SELECT * FROM predictions ORDER BY id DESC";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfMatchId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "matchId");
        final int _columnIndexOfTeamTag = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "teamTag");
        final int _columnIndexOfScore = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "score");
        final int _columnIndexOfCoefficient = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "coefficient");
        final int _columnIndexOfStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "status");
        final List<PredictionEntity> _result = new ArrayList<PredictionEntity>();
        while (_stmt.step()) {
          final PredictionEntity _item;
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          final int _tmpMatchId;
          _tmpMatchId = (int) (_stmt.getLong(_columnIndexOfMatchId));
          final String _tmpTeamTag;
          if (_stmt.isNull(_columnIndexOfTeamTag)) {
            _tmpTeamTag = null;
          } else {
            _tmpTeamTag = _stmt.getText(_columnIndexOfTeamTag);
          }
          final String _tmpScore;
          if (_stmt.isNull(_columnIndexOfScore)) {
            _tmpScore = null;
          } else {
            _tmpScore = _stmt.getText(_columnIndexOfScore);
          }
          final double _tmpCoefficient;
          _tmpCoefficient = _stmt.getDouble(_columnIndexOfCoefficient);
          final String _tmpStatus;
          if (_stmt.isNull(_columnIndexOfStatus)) {
            _tmpStatus = null;
          } else {
            _tmpStatus = _stmt.getText(_columnIndexOfStatus);
          }
          _item = new PredictionEntity(_tmpId,_tmpMatchId,_tmpTeamTag,_tmpScore,_tmpCoefficient,_tmpStatus);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void deleteAll() {
    final String _sql = "DELETE FROM predictions";
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
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
