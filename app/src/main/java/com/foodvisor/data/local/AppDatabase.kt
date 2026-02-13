package com.foodvisor.data.local

import android.content.Context
import androidx.room.*
import com.foodvisor.data.model.Restaurant
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Database(entities = [Restaurant::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun restaurantDao(): RestaurantDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "foodvisor_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

@Dao
interface RestaurantDao {

    @Query("SELECT * FROM restaurants ORDER BY nom ASC")
    suspend fun getAllRestaurants(): List<Restaurant>

    @Query("SELECT * FROM restaurants WHERE id = :id")
    suspend fun getRestaurantById(id: String): Restaurant?

    @Query("SELECT * FROM restaurants WHERE isFavorite = 1 ORDER BY nom ASC")
    suspend fun getFavorites(): List<Restaurant>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(restaurants: List<Restaurant>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(restaurant: Restaurant)

    @Update
    suspend fun update(restaurant: Restaurant)

    @Delete
    suspend fun delete(restaurant: Restaurant)

    @Query("DELETE FROM restaurants")
    suspend fun deleteAll()

    @Query("UPDATE restaurants SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean)

    @Query("SELECT * FROM restaurants WHERE " +
            "nom LIKE '%' || :query || '%' OR " +
            "cuisine LIKE '%' || :query || '%' OR " +
            "description LIKE '%' || :query || '%'")
    suspend fun searchRestaurants(query: String): List<Restaurant>
}

class Converters {

    @TypeConverter
    fun fromStringMap(value: Map<String, String>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringMap(value: String?): Map<String, String>? {
        if (value == null) return null
        val mapType = object : TypeToken<Map<String, String>>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        if (value == null) return null
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }
}