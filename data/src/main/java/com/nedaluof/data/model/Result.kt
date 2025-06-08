package com.nedaluof.data.model

/**
 * Created by nedaluof on 12/11/2020.
 */
sealed class DataResult<out T>(
  val data: T? = null,
  val error: String? = null
) {
  class Success<T>(data: T? = null) : DataResult<T>(data)
  class Error<T>(error: String? = null) : DataResult<T>(error = error)
}