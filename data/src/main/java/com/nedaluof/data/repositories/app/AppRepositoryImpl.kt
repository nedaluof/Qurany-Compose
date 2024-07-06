package com.nedaluof.data.repositories.app

import com.nedaluof.data.datasource.localsource.preferences.PreferencesKeys.LANGUAGE_KEY
import com.nedaluof.data.datasource.localsource.preferences.PreferencesKeys.NIGHT_MODE_KEY
import com.nedaluof.data.datasource.localsource.preferences.PreferencesManager
import javax.inject.Inject

/**
 * Created by NedaluOf on 12/9/2022.
 */
class AppRepositoryImpl @Inject constructor(
  private val preferencesManager: PreferencesManager
) : AppRepository {

  override fun isNightModeEnabled(): Boolean {
    return preferencesManager.getFromPreferences(NIGHT_MODE_KEY, false) ?: false
  }

  override fun isCurrentLanguageEnglish(): Boolean {
    return (preferencesManager.getFromPreferences<String>(LANGUAGE_KEY, "ar") ?: "ar") == "en"
  }

  override fun updateNightMode(isEnabled: Boolean) {
    preferencesManager.addToPreferences(NIGHT_MODE_KEY, isEnabled)
  }

  override fun updateCurrentLanguage() {
    val newLocale = if (isCurrentLanguageEnglish()) "ar" else "en"
    preferencesManager.addToPreferences(LANGUAGE_KEY, newLocale)
  }
}