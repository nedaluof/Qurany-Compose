package com.nedaluof.data.datasource.localsource.preferences

import android.content.Context
import com.nedaluof.data.BuildConfig
import com.nedaluof.mihawk.MiHawk
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by nedaluof on 6/23/2020. {Java}
 * Created by nedaluof on 12/17/2020. {Kotlin}
 * Updated by nedaluof on 9/17/2021. {From [PreferenceManager] to [preferencesDataStore]}
 * Updated by nedaluof on 12/9/2022. {From [PreferenceManager] to [MiHawk]}
 */

@Singleton
class PreferencesManager @Inject constructor(
  @ApplicationContext private val context: Context
) {

  init {
    MiHawk.Builder(context)
      .withLoggingEnabled(BuildConfig.DEBUG)
      .withPreferenceName("QURANY_PREFERENCES")
      .build()
  }

  inline fun <reified T> addToPreferences(key: String, value: T) {
    MiHawk.put(key, value)
  }

  inline fun <reified T> getFromPreferences(key: String, default: T? = null): T? {
    return MiHawk.get(key, default)
  }
}