package com.nedaluof.data.di

import com.nedaluof.data.repositories.app.AppRepository
import com.nedaluof.data.repositories.app.AppRepositoryImpl
import dagger.Binds
import dagger.Module
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