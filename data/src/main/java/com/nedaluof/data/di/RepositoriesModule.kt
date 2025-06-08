package com.nedaluof.data.di

import com.nedaluof.data.repositories.app.AppRepository
import com.nedaluof.data.repositories.app.AppRepositoryImpl
import com.nedaluof.data.repositories.reciters.RecitersRepository
import com.nedaluof.data.repositories.reciters.RecitersRepositoryImpl
import com.nedaluof.data.repositories.suras.SurasRepository
import com.nedaluof.data.repositories.suras.SurasRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by NedaluOf on 8/16/2021.
 */
@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoriesModule {

  @Singleton
  @Binds
  abstract fun bindAppRepository(
    impl: AppRepositoryImpl
  ): AppRepository

  @Binds
  abstract fun bindRecitersRepository(
    repoImpl: RecitersRepositoryImpl
  ): RecitersRepository

  @Binds
  abstract fun bindSurasRepository(
    repoImpl: SurasRepositoryImpl
  ): SurasRepository
}