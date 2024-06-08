package com.nedaluof.data.di

import com.nedaluof.data.datasource.remotesource.api.ApiService
import com.nedaluof.data.datasource.remotesource.api.ApiService.Companion.BASE_URL
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by nedaluof on 12/11/2020.
 */
@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

  @Singleton
  @Provides
  fun provideHttpLoggingInterceptor() =
    HttpLoggingInterceptor().apply {
      level = HttpLoggingInterceptor.Level.BODY
    }

  @Singleton
  @Provides
  fun provideOkHttpClient(interceptor: HttpLoggingInterceptor) =
    OkHttpClient.Builder()
      .addInterceptor(interceptor)
      .readTimeout(180, TimeUnit.SECONDS)
      .connectTimeout(180, TimeUnit.SECONDS)
      .build()

  @Singleton
  @Provides
  fun provideMoshi(): Moshi = Moshi.Builder().build()

  @Singleton
  @Provides
  fun provideRetrofitClient(client: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(client)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

  @Singleton
  @Provides
  fun provideApiService(client: Retrofit): ApiService =
    client.create(ApiService::class.java)
}