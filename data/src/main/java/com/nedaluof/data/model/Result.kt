package com.nedaluof.data.model

/**
 * Created by nedaluof on 12/11/2020.
 */
sealed class DataResult<out T>(
  val data: T? = null,
  val error: String? = null,
  val loading: Boolean = false
) {
  class Loading<T>(loading: Boolean) : DataResult<T>(loading = loading)
  class Success<T>(data: T? = null) : DataResult<T>(data)
  class Error<T>(error: String? = null) : DataResult<T>(error = error)
}