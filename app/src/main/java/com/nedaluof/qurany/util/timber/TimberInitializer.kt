package com.nedaluof.qurany.util.timber

import android.content.Context
import androidx.startup.Initializer
import com.nedaluof.qurany.BuildConfig
import timber.log.Timber

/**
 * Created by nedaluof on 1/1/2021.
 */
class TimberInitializer : Initializer<Unit> {
  override fun create(context: Context) {
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    } else {
      Timber.plant(ReleaseTree())
    }
  }

  override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
