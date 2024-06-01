package com.nedaluof.qurany.di

import com.nedaluof.qurany.data.repository.RecitersRepository
import com.nedaluof.qurany.data.repository.SplashRepository
import com.nedaluof.qurany.data.repository.SurasRepository
import com.nedaluof.qurany.data.repositoryImpl.RecitersRepositoryImpl
import com.nedaluof.qurany.data.repositoryImpl.SplashRepositoryImpl
import com.nedaluof.qurany.data.repositoryImpl.SurasRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by NedaluOf on 8/16/2021.
 */
@ExperimentalCoroutinesApi
@InstallIn(ViewModelComponent::class)
@Module
abstract class RepositoriesModule {

  @ViewModelScoped
  @Binds
  abstract fun bindSplashRepository(
    repoImpl: SplashRepositoryImpl
  ): SplashRepository

  @ViewModelScoped
  @Binds
  abstract fun bindRecitersRepository(
    repoImpl: RecitersRepositoryImpl
  ): RecitersRepository

  @ViewModelScoped
  @Binds
  abstract fun bindSurasRepository(
    repoImpl: SurasRepositoryImpl
  ): SurasRepository
}