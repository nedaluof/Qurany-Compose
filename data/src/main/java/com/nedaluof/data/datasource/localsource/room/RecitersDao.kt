package com.nedaluof.data.datasource.localsource.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nedaluof.data.model.ReciterEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by nedaluof on 12/11/2020.
 * Updated by nedaluof on 6/9/2024.
 */
@Dao
interface RecitersDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertReciters(list: List<ReciterEntity>)

  @Query("select * from reciters_table order by name")
  fun loadReciters(): Flow<List<ReciterEntity>>

  @Query("select * from reciters_table where isInMyFavorites order by name")
  fun loadFavoriteReciters(): Flow<List<ReciterEntity>>

  @Query("UPDATE reciters_table SET isInMyFavorites=:isInMyFavorites WHERE reciterId = :reciterId")
  suspend fun updateReciter(isInMyFavorites: Boolean, reciterId: Int)

  @Query("SELECT COUNT(*) FROM reciters_table")
  suspend fun loadRecitersCount(): Int
}