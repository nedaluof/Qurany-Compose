package com.nedaluof.data.util

import timber.log.Timber

/**
 * Created By NedaluOf - 6/9/2024.
 */

fun catchOn(
  tryBlock: () -> Unit, exceptionBlock: (Exception) -> Unit = {}
) {
  try {
    tryBlock()
  } catch (e: Exception) {
    e.message.loge()
    exceptionBlock(e)
  }
}

suspend fun catchOnSuspend(
  tryBlock: suspend () -> Unit,
  exceptionBlock: (Exception) -> Unit = {}
) {
  try {
    tryBlock()
  } catch (e: Exception) {
    e.message.loge()
    exceptionBlock(e)
  }
}

fun String?.loge() = Timber.e("NedaluOf -> $this")