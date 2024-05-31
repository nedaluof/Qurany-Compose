package com.nedaluof.qurany.util.timber

import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

/**
 * Created by nedaluof on 1/1/2021.
 */
class ReleaseTree : Timber.Tree() {
  override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
    FirebaseCrashlytics.getInstance().recordException(t!!)
  }
}