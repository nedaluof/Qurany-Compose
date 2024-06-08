package com.nedaluof.data.datasource.localsource.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nedaluof.data.model.Reciter

/**
 * Created by nedaluof on 12/2/2020.
 */
@Database(entities = [Reciter::class], version = 1, exportSchema = false)
abstract class QuranyDatabase : RoomDatabase() {

  abstract val recitersDao: RecitersDao
}