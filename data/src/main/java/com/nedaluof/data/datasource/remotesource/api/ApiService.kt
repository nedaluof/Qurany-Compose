package com.nedaluof.data.datasource.remotesource.api

import com.nedaluof.data.model.RecitersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by nedaluof on 12/11/2020.
 */
interface ApiService {

  @GET("{language}.php")
  suspend fun getReciters(
    @Path("language") language: String
  ): Response<RecitersResponse>

  companion object {
    const val BASE_URL = "http://mp3quran.net/api/"
  }
}