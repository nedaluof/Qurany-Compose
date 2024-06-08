package com.nedaluof.data.di

import com.nedaluof.data.repositories.reciters.RecitersRepository
import com.nedaluof.data.repositories.reciters.RecitersRepositoryImpl
import com.nedaluof.data.repositories.suras.SurasRepository
import com.nedaluof.data.repositories.suras.SurasRepositoryImpl
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
  abstract fun bindRecitersRepository(
    repoImpl: RecitersRepositoryImpl
  ): RecitersRepository

  @ViewModelScoped
  @Binds
  abstract fun bindSurasRepository(
    repoImpl: SurasRepositoryImpl
  ): SurasRepository
}