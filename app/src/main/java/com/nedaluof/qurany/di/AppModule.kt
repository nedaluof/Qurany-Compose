package com.nedaluof.qurany.di

import com.nedaluof.qurany.data.repository.SettingsRepository
import com.nedaluof.qurany.data.repositoryImpl.SettingsRepositoryImpl
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
  abstract fun bindSettingsRepository(
    impl: SettingsRepositoryImpl
  ): SettingsRepository
}

/**
 * Inject LocaleManager instance in classes that
 * processed before the Hilt inject them with right instances
 * */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface LocaleManagerEntryPoint {
  val localeManager: LocaleManager
}