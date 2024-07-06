package com.nedaluof.data.datasource.localsource.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nedaluof.data.model.LocalTextTypeConverter
import com.nedaluof.data.model.ReciterEntity

/**
 * Created by nedaluof on 12/2/2020.
 */
@Database(entities = [ReciterEntity::class], version = 1, exportSchema = false)
@TypeConverters(LocalTextTypeConverter::class)
abstract class QuranyDatabase : RoomDatabase() {
  abstract val recitersDao: RecitersDao
}