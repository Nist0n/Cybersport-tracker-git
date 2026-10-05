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
import ru.mirea.pavlovve.cybersporttracker.data.local.entity.FavoriteTeamEntity;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class FavoriteTeamDao_Impl implements FavoriteTeamDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<FavoriteTeamEntity> __insertAdapterOfFavoriteTeamEntity;

  public FavoriteTeamDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfFavoriteTeamEntity = new EntityInsertAdapter<FavoriteTeamEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `favorite_teams` (`id`,`tag`,`name`,`country`,`logoUrl`,`worldRank`,`players`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          final FavoriteTeamEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getTag() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getTag());
        }
        if (entity.getName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getName());
        }
        if (entity.getCountry() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getCountry());
        }
        if (entity.getLogoUrl() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getLogoUrl());
        }
        statement.bindLong(6, entity.getWorldRank());
        if (entity.getPlayers() == null) {
          statement.bindNull(7);
        } else {
          statement.bindText(7, entity.getPlayers());
        }
      }
    };
  }

  @Override
  public void insert(final FavoriteTeamEntity entity) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfFavoriteTeamEntity.insert(_connection, entity);
      return null;
    });
  }

  @Override
  public List<FavoriteTeamEntity> getAll() {
    final String _sql = "SELECT * FROM favorite_teams";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfTag = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tag");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfCountry = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "country");
        final int _columnIndexOfLogoUrl = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "logoUrl");
        final int _columnIndexOfWorldRank = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "worldRank");
        final int _columnIndexOfPlayers = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "players");
        final List<FavoriteTeamEntity> _result = new ArrayList<FavoriteTeamEntity>();
        while (_stmt.step()) {
          final FavoriteTeamEntity _item;
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          final String _tmpTag;
          if (_stmt.isNull(_columnIndexOfTag)) {
            _tmpTag = null;
          } else {
            _tmpTag = _stmt.getText(_columnIndexOfTag);
          }
          final String _tmpName;
          if (_stmt.isNull(_columnIndexOfName)) {
            _tmpName = null;
          } else {
            _tmpName = _stmt.getText(_columnIndexOfName);
          }
          final String _tmpCountry;
          if (_stmt.isNull(_columnIndexOfCountry)) {
            _tmpCountry = null;
          } else {
            _tmpCountry = _stmt.getText(_columnIndexOfCountry);
          }
          final String _tmpLogoUrl;
          if (_stmt.isNull(_columnIndexOfLogoUrl)) {
            _tmpLogoUrl = null;
          } else {
            _tmpLogoUrl = _stmt.getText(_columnIndexOfLogoUrl);
          }
          final int _tmpWorldRank;
          _tmpWorldRank = (int) (_stmt.getLong(_columnIndexOfWorldRank));
          final String _tmpPlayers;
          if (_stmt.isNull(_columnIndexOfPlayers)) {
            _tmpPlayers = null;
          } else {
            _tmpPlayers = _stmt.getText(_columnIndexOfPlayers);
          }
          _item = new FavoriteTeamEntity(_tmpId,_tmpTag,_tmpName,_tmpCountry,_tmpLogoUrl,_tmpWorldRank,_tmpPlayers);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void deleteById(final int teamId) {
    final String _sql = "DELETE FROM favorite_teams WHERE id = ?";
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, teamId);
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
