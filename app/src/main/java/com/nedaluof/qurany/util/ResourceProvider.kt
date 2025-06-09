package com.nedaluof.qurany.util

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject

/**
 * Created By NedaluOf - 09/06/2025.
 */
class ResourceProvider @Inject constructor(
  @ApplicationContext private val context: Context
) {
  //region logic
  fun provideString(stringId: Int): String {
    val defaultLocale = Locale("en")
    val locale = try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.getSystemService(LocaleManager::class.java).applicationLocales[0] ?: defaultLocale
      } else {
        AppCompatDelegate.getApplicationLocales()[0] ?: defaultLocale
      }
    } catch (_: Exception) {
      defaultLocale
    }
    val configurations = context.resources.configuration
    configurations.setLocale(locale)
    val fixedContext = context.createConfigurationContext(configurations)
    return fixedContext.getString(stringId)
  }
  //endregion
}