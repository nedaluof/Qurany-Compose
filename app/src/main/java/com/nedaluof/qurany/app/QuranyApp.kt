package com.nedaluof.qurany.app

import android.app.Application
import com.nedaluof.qurany.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Created by nedaluof on 11/29/2020.
 */
@HiltAndroidApp
class QuranyApp : Application() {
  override fun onCreate() {
    super.onCreate()
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
  }
}