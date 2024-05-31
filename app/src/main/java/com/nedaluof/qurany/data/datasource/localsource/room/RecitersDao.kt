package com.nedaluof.qurany.data.datasource.localsource.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nedaluof.qurany.data.model.Reciter
import kotlinx.coroutines.flow.Flow

/**
 * Created by nedaluof on 12/11/2020.
 */
@Dao
interface RecitersDao {
  /*For Future use Todo (Caching) */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertReciters(list: List<Reciter>)

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insertReciter(reciter: Reciter)

  /* Used in MyReciters */
  @Query("select * from reciter order by name")
  fun getMyReciters(): Flow<List<Reciter>>

  // to check records number
  @Query("SELECT COUNT(*) FROM reciter")
  fun getRecitersRecordsNumber(): Int

  /* For Future use Todo (setting delete All Reciters in My Reciters) */
  @Query("Delete from reciter")
  suspend fun deleteAllReciters()

  /* Used in MyReciters */
  @Delete
  suspend fun deleteReciter(reciter: Reciter)
}