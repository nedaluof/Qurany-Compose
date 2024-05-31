package com.nedaluof.qurany.di

import android.content.Context
import androidx.room.Room
import com.nedaluof.qurany.data.datasource.localsource.room.QuranyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by nedaluof on 12/2/2020.
 */
@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
  @Singleton
  @Provides
  fun provideDatabase(@ApplicationContext context: Context) =
    Room.databaseBuilder(
      context,
      QuranyDatabase::class.java,
      QuranyDatabase.DB_NAME
    ).build()

  @Singleton
  @Provides
  fun provideReciterDao(database: QuranyDatabase) =
    database.getRecitersDao()
}
