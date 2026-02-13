package com.foodvisor.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.foodvisor.data.model.Restaurant;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RestaurantDao_Impl implements RestaurantDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Restaurant> __insertionAdapterOfRestaurant;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<Restaurant> __deletionAdapterOfRestaurant;

  private final EntityDeletionOrUpdateAdapter<Restaurant> __updateAdapterOfRestaurant;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final SharedSQLiteStatement __preparedStmtOfUpdateFavoriteStatus;

  public RestaurantDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRestaurant = new EntityInsertionAdapter<Restaurant>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `restaurants` (`id`,`nom`,`description`,`cuisine`,`prixMoyen`,`noteGoogle`,`latitude`,`longitude`,`adresse`,`telephone`,`url`,`isFavorite`,`etoilesMichelin`,`horaires`,`thumbImageUrl`,`featuredImageUrl`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Restaurant entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getNom());
        statement.bindString(3, entity.getDescription());
        statement.bindString(4, entity.getCuisine());
        statement.bindDouble(5, entity.getPrixMoyen());
        statement.bindDouble(6, entity.getNoteGoogle());
        statement.bindDouble(7, entity.getLatitude());
        statement.bindDouble(8, entity.getLongitude());
        statement.bindString(9, entity.getAdresse());
        statement.bindString(10, entity.getTelephone());
        statement.bindString(11, entity.getUrl());
        final int _tmp = entity.isFavorite() ? 1 : 0;
        statement.bindLong(12, _tmp);
        statement.bindLong(13, entity.getEtoilesMichelin());
        final String _tmp_1 = __converters.fromStringMap(entity.getHoraires());
        if (_tmp_1 == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, _tmp_1);
        }
        final String _tmp_2 = __converters.fromStringList(entity.getThumbImageUrl());
        if (_tmp_2 == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, _tmp_2);
        }
        if (entity.getFeaturedImageUrl() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getFeaturedImageUrl());
        }
      }
    };
    this.__deletionAdapterOfRestaurant = new EntityDeletionOrUpdateAdapter<Restaurant>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `restaurants` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Restaurant entity) {
        statement.bindString(1, entity.getId());
      }
    };
    this.__updateAdapterOfRestaurant = new EntityDeletionOrUpdateAdapter<Restaurant>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `restaurants` SET `id` = ?,`nom` = ?,`description` = ?,`cuisine` = ?,`prixMoyen` = ?,`noteGoogle` = ?,`latitude` = ?,`longitude` = ?,`adresse` = ?,`telephone` = ?,`url` = ?,`isFavorite` = ?,`etoilesMichelin` = ?,`horaires` = ?,`thumbImageUrl` = ?,`featuredImageUrl` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Restaurant entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getNom());
        statement.bindString(3, entity.getDescription());
        statement.bindString(4, entity.getCuisine());
        statement.bindDouble(5, entity.getPrixMoyen());
        statement.bindDouble(6, entity.getNoteGoogle());
        statement.bindDouble(7, entity.getLatitude());
        statement.bindDouble(8, entity.getLongitude());
        statement.bindString(9, entity.getAdresse());
        statement.bindString(10, entity.getTelephone());
        statement.bindString(11, entity.getUrl());
        final int _tmp = entity.isFavorite() ? 1 : 0;
        statement.bindLong(12, _tmp);
        statement.bindLong(13, entity.getEtoilesMichelin());
        final String _tmp_1 = __converters.fromStringMap(entity.getHoraires());
        if (_tmp_1 == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, _tmp_1);
        }
        final String _tmp_2 = __converters.fromStringList(entity.getThumbImageUrl());
        if (_tmp_2 == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, _tmp_2);
        }
        if (entity.getFeaturedImageUrl() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getFeaturedImageUrl());
        }
        statement.bindString(17, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM restaurants";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateFavoriteStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE restaurants SET isFavorite = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertAll(final List<Restaurant> restaurants,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRestaurant.insert(restaurants);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insert(final Restaurant restaurant, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRestaurant.insert(restaurant);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final Restaurant restaurant, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfRestaurant.handle(restaurant);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final Restaurant restaurant, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfRestaurant.handle(restaurant);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateFavoriteStatus(final String id, final boolean isFavorite,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateFavoriteStatus.acquire();
        int _argIndex = 1;
        final int _tmp = isFavorite ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateFavoriteStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllRestaurants(final Continuation<? super List<Restaurant>> $completion) {
    final String _sql = "SELECT * FROM restaurants ORDER BY nom ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Restaurant>>() {
      @Override
      @NonNull
      public List<Restaurant> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNom = CursorUtil.getColumnIndexOrThrow(_cursor, "nom");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCuisine = CursorUtil.getColumnIndexOrThrow(_cursor, "cuisine");
          final int _cursorIndexOfPrixMoyen = CursorUtil.getColumnIndexOrThrow(_cursor, "prixMoyen");
          final int _cursorIndexOfNoteGoogle = CursorUtil.getColumnIndexOrThrow(_cursor, "noteGoogle");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfAdresse = CursorUtil.getColumnIndexOrThrow(_cursor, "adresse");
          final int _cursorIndexOfTelephone = CursorUtil.getColumnIndexOrThrow(_cursor, "telephone");
          final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "url");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfEtoilesMichelin = CursorUtil.getColumnIndexOrThrow(_cursor, "etoilesMichelin");
          final int _cursorIndexOfHoraires = CursorUtil.getColumnIndexOrThrow(_cursor, "horaires");
          final int _cursorIndexOfThumbImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbImageUrl");
          final int _cursorIndexOfFeaturedImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "featuredImageUrl");
          final List<Restaurant> _result = new ArrayList<Restaurant>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Restaurant _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpNom;
            _tmpNom = _cursor.getString(_cursorIndexOfNom);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpCuisine;
            _tmpCuisine = _cursor.getString(_cursorIndexOfCuisine);
            final double _tmpPrixMoyen;
            _tmpPrixMoyen = _cursor.getDouble(_cursorIndexOfPrixMoyen);
            final double _tmpNoteGoogle;
            _tmpNoteGoogle = _cursor.getDouble(_cursorIndexOfNoteGoogle);
            final double _tmpLatitude;
            _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            final double _tmpLongitude;
            _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            final String _tmpAdresse;
            _tmpAdresse = _cursor.getString(_cursorIndexOfAdresse);
            final String _tmpTelephone;
            _tmpTelephone = _cursor.getString(_cursorIndexOfTelephone);
            final String _tmpUrl;
            _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final int _tmpEtoilesMichelin;
            _tmpEtoilesMichelin = _cursor.getInt(_cursorIndexOfEtoilesMichelin);
            final Map<String, String> _tmpHoraires;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfHoraires)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfHoraires);
            }
            _tmpHoraires = __converters.toStringMap(_tmp_1);
            final List<String> _tmpThumbImageUrl;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfThumbImageUrl)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfThumbImageUrl);
            }
            _tmpThumbImageUrl = __converters.toStringList(_tmp_2);
            final String _tmpFeaturedImageUrl;
            if (_cursor.isNull(_cursorIndexOfFeaturedImageUrl)) {
              _tmpFeaturedImageUrl = null;
            } else {
              _tmpFeaturedImageUrl = _cursor.getString(_cursorIndexOfFeaturedImageUrl);
            }
            _item = new Restaurant(_tmpId,_tmpNom,_tmpDescription,_tmpCuisine,_tmpPrixMoyen,_tmpNoteGoogle,_tmpLatitude,_tmpLongitude,_tmpAdresse,_tmpTelephone,_tmpUrl,_tmpIsFavorite,_tmpEtoilesMichelin,_tmpHoraires,_tmpThumbImageUrl,_tmpFeaturedImageUrl);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getRestaurantById(final String id,
      final Continuation<? super Restaurant> $completion) {
    final String _sql = "SELECT * FROM restaurants WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Restaurant>() {
      @Override
      @Nullable
      public Restaurant call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNom = CursorUtil.getColumnIndexOrThrow(_cursor, "nom");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCuisine = CursorUtil.getColumnIndexOrThrow(_cursor, "cuisine");
          final int _cursorIndexOfPrixMoyen = CursorUtil.getColumnIndexOrThrow(_cursor, "prixMoyen");
          final int _cursorIndexOfNoteGoogle = CursorUtil.getColumnIndexOrThrow(_cursor, "noteGoogle");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfAdresse = CursorUtil.getColumnIndexOrThrow(_cursor, "adresse");
          final int _cursorIndexOfTelephone = CursorUtil.getColumnIndexOrThrow(_cursor, "telephone");
          final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "url");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfEtoilesMichelin = CursorUtil.getColumnIndexOrThrow(_cursor, "etoilesMichelin");
          final int _cursorIndexOfHoraires = CursorUtil.getColumnIndexOrThrow(_cursor, "horaires");
          final int _cursorIndexOfThumbImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbImageUrl");
          final int _cursorIndexOfFeaturedImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "featuredImageUrl");
          final Restaurant _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpNom;
            _tmpNom = _cursor.getString(_cursorIndexOfNom);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpCuisine;
            _tmpCuisine = _cursor.getString(_cursorIndexOfCuisine);
            final double _tmpPrixMoyen;
            _tmpPrixMoyen = _cursor.getDouble(_cursorIndexOfPrixMoyen);
            final double _tmpNoteGoogle;
            _tmpNoteGoogle = _cursor.getDouble(_cursorIndexOfNoteGoogle);
            final double _tmpLatitude;
            _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            final double _tmpLongitude;
            _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            final String _tmpAdresse;
            _tmpAdresse = _cursor.getString(_cursorIndexOfAdresse);
            final String _tmpTelephone;
            _tmpTelephone = _cursor.getString(_cursorIndexOfTelephone);
            final String _tmpUrl;
            _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final int _tmpEtoilesMichelin;
            _tmpEtoilesMichelin = _cursor.getInt(_cursorIndexOfEtoilesMichelin);
            final Map<String, String> _tmpHoraires;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfHoraires)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfHoraires);
            }
            _tmpHoraires = __converters.toStringMap(_tmp_1);
            final List<String> _tmpThumbImageUrl;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfThumbImageUrl)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfThumbImageUrl);
            }
            _tmpThumbImageUrl = __converters.toStringList(_tmp_2);
            final String _tmpFeaturedImageUrl;
            if (_cursor.isNull(_cursorIndexOfFeaturedImageUrl)) {
              _tmpFeaturedImageUrl = null;
            } else {
              _tmpFeaturedImageUrl = _cursor.getString(_cursorIndexOfFeaturedImageUrl);
            }
            _result = new Restaurant(_tmpId,_tmpNom,_tmpDescription,_tmpCuisine,_tmpPrixMoyen,_tmpNoteGoogle,_tmpLatitude,_tmpLongitude,_tmpAdresse,_tmpTelephone,_tmpUrl,_tmpIsFavorite,_tmpEtoilesMichelin,_tmpHoraires,_tmpThumbImageUrl,_tmpFeaturedImageUrl);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getFavorites(final Continuation<? super List<Restaurant>> $completion) {
    final String _sql = "SELECT * FROM restaurants WHERE isFavorite = 1 ORDER BY nom ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Restaurant>>() {
      @Override
      @NonNull
      public List<Restaurant> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNom = CursorUtil.getColumnIndexOrThrow(_cursor, "nom");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCuisine = CursorUtil.getColumnIndexOrThrow(_cursor, "cuisine");
          final int _cursorIndexOfPrixMoyen = CursorUtil.getColumnIndexOrThrow(_cursor, "prixMoyen");
          final int _cursorIndexOfNoteGoogle = CursorUtil.getColumnIndexOrThrow(_cursor, "noteGoogle");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfAdresse = CursorUtil.getColumnIndexOrThrow(_cursor, "adresse");
          final int _cursorIndexOfTelephone = CursorUtil.getColumnIndexOrThrow(_cursor, "telephone");
          final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "url");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfEtoilesMichelin = CursorUtil.getColumnIndexOrThrow(_cursor, "etoilesMichelin");
          final int _cursorIndexOfHoraires = CursorUtil.getColumnIndexOrThrow(_cursor, "horaires");
          final int _cursorIndexOfThumbImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbImageUrl");
          final int _cursorIndexOfFeaturedImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "featuredImageUrl");
          final List<Restaurant> _result = new ArrayList<Restaurant>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Restaurant _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpNom;
            _tmpNom = _cursor.getString(_cursorIndexOfNom);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpCuisine;
            _tmpCuisine = _cursor.getString(_cursorIndexOfCuisine);
            final double _tmpPrixMoyen;
            _tmpPrixMoyen = _cursor.getDouble(_cursorIndexOfPrixMoyen);
            final double _tmpNoteGoogle;
            _tmpNoteGoogle = _cursor.getDouble(_cursorIndexOfNoteGoogle);
            final double _tmpLatitude;
            _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            final double _tmpLongitude;
            _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            final String _tmpAdresse;
            _tmpAdresse = _cursor.getString(_cursorIndexOfAdresse);
            final String _tmpTelephone;
            _tmpTelephone = _cursor.getString(_cursorIndexOfTelephone);
            final String _tmpUrl;
            _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final int _tmpEtoilesMichelin;
            _tmpEtoilesMichelin = _cursor.getInt(_cursorIndexOfEtoilesMichelin);
            final Map<String, String> _tmpHoraires;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfHoraires)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfHoraires);
            }
            _tmpHoraires = __converters.toStringMap(_tmp_1);
            final List<String> _tmpThumbImageUrl;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfThumbImageUrl)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfThumbImageUrl);
            }
            _tmpThumbImageUrl = __converters.toStringList(_tmp_2);
            final String _tmpFeaturedImageUrl;
            if (_cursor.isNull(_cursorIndexOfFeaturedImageUrl)) {
              _tmpFeaturedImageUrl = null;
            } else {
              _tmpFeaturedImageUrl = _cursor.getString(_cursorIndexOfFeaturedImageUrl);
            }
            _item = new Restaurant(_tmpId,_tmpNom,_tmpDescription,_tmpCuisine,_tmpPrixMoyen,_tmpNoteGoogle,_tmpLatitude,_tmpLongitude,_tmpAdresse,_tmpTelephone,_tmpUrl,_tmpIsFavorite,_tmpEtoilesMichelin,_tmpHoraires,_tmpThumbImageUrl,_tmpFeaturedImageUrl);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object searchRestaurants(final String query,
      final Continuation<? super List<Restaurant>> $completion) {
    final String _sql = "SELECT * FROM restaurants WHERE nom LIKE '%' || ? || '%' OR cuisine LIKE '%' || ? || '%' OR description LIKE '%' || ? || '%'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    _argIndex = 2;
    _statement.bindString(_argIndex, query);
    _argIndex = 3;
    _statement.bindString(_argIndex, query);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Restaurant>>() {
      @Override
      @NonNull
      public List<Restaurant> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNom = CursorUtil.getColumnIndexOrThrow(_cursor, "nom");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCuisine = CursorUtil.getColumnIndexOrThrow(_cursor, "cuisine");
          final int _cursorIndexOfPrixMoyen = CursorUtil.getColumnIndexOrThrow(_cursor, "prixMoyen");
          final int _cursorIndexOfNoteGoogle = CursorUtil.getColumnIndexOrThrow(_cursor, "noteGoogle");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfAdresse = CursorUtil.getColumnIndexOrThrow(_cursor, "adresse");
          final int _cursorIndexOfTelephone = CursorUtil.getColumnIndexOrThrow(_cursor, "telephone");
          final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "url");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfEtoilesMichelin = CursorUtil.getColumnIndexOrThrow(_cursor, "etoilesMichelin");
          final int _cursorIndexOfHoraires = CursorUtil.getColumnIndexOrThrow(_cursor, "horaires");
          final int _cursorIndexOfThumbImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbImageUrl");
          final int _cursorIndexOfFeaturedImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "featuredImageUrl");
          final List<Restaurant> _result = new ArrayList<Restaurant>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Restaurant _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpNom;
            _tmpNom = _cursor.getString(_cursorIndexOfNom);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpCuisine;
            _tmpCuisine = _cursor.getString(_cursorIndexOfCuisine);
            final double _tmpPrixMoyen;
            _tmpPrixMoyen = _cursor.getDouble(_cursorIndexOfPrixMoyen);
            final double _tmpNoteGoogle;
            _tmpNoteGoogle = _cursor.getDouble(_cursorIndexOfNoteGoogle);
            final double _tmpLatitude;
            _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            final double _tmpLongitude;
            _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            final String _tmpAdresse;
            _tmpAdresse = _cursor.getString(_cursorIndexOfAdresse);
            final String _tmpTelephone;
            _tmpTelephone = _cursor.getString(_cursorIndexOfTelephone);
            final String _tmpUrl;
            _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final int _tmpEtoilesMichelin;
            _tmpEtoilesMichelin = _cursor.getInt(_cursorIndexOfEtoilesMichelin);
            final Map<String, String> _tmpHoraires;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfHoraires)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfHoraires);
            }
            _tmpHoraires = __converters.toStringMap(_tmp_1);
            final List<String> _tmpThumbImageUrl;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfThumbImageUrl)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfThumbImageUrl);
            }
            _tmpThumbImageUrl = __converters.toStringList(_tmp_2);
            final String _tmpFeaturedImageUrl;
            if (_cursor.isNull(_cursorIndexOfFeaturedImageUrl)) {
              _tmpFeaturedImageUrl = null;
            } else {
              _tmpFeaturedImageUrl = _cursor.getString(_cursorIndexOfFeaturedImageUrl);
            }
            _item = new Restaurant(_tmpId,_tmpNom,_tmpDescription,_tmpCuisine,_tmpPrixMoyen,_tmpNoteGoogle,_tmpLatitude,_tmpLongitude,_tmpAdresse,_tmpTelephone,_tmpUrl,_tmpIsFavorite,_tmpEtoilesMichelin,_tmpHoraires,_tmpThumbImageUrl,_tmpFeaturedImageUrl);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
