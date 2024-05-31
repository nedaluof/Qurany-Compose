package com.nedaluof.qurany.data.model

/**
 * Created by nedaluof on 12/11/2020.
 */
data class Result<out T>(val status: Status, val data: T?, val message: String?) {
  companion object {
    fun <T> success(data: T): Result<T> =
      Result(status = Status.SUCCESS, data = data, message = null)

    fun <T> error(data: T?, message: String): Result<T> =
      Result(status = Status.ERROR, data = data, message = message)
  }
}

enum class Status {
  SUCCESS,
  ERROR;
}
