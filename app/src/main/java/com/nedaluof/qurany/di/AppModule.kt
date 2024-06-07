package com.nedaluof.qurany.di

import com.nedaluof.qurany.data.repositories.app.AppRepository
import com.nedaluof.qurany.data.repositories.app.AppRepositoryImpl
import com.nedaluof.qurany.util.LocaleManager
import dagger.Binds
import dagger.Module
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by nedaluof on 12/11/2020.
 * Updated by nedaluof on 9/18/2021.
 */
@InstallIn(SingletonComponent::class)
@Module
abstract class AppModule {

  @Binds
  abstract fun bindAppRepository(
    impl: AppRepositoryImpl
  ): AppRepository
}

/**
 * Inject LocaleManager instance in classes that
 * processed before Hilt inject them with right instances
 * */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface LocaleManagerEntryPoint {
  val localeManager: LocaleManager
}