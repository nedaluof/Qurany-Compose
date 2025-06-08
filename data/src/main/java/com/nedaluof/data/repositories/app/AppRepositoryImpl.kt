package com.nedaluof.data.repositories.app

import com.nedaluof.data.datasource.localsource.preferences.PreferencesKeys.LANGUAGE_KEY
import com.nedaluof.data.datasource.localsource.preferences.PreferencesKeys.NIGHT_MODE_KEY
import com.nedaluof.data.datasource.localsource.preferences.PreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by NedaluOf on 12/9/2022.
 */
@Singleton
class AppRepositoryImpl @Inject constructor(
  private val preferencesManager: PreferencesManager
) : AppRepository {

  //region variables
  private val isNightModeEnabledFlow = MutableStateFlow(false)
  //endregion

  //region logic
  override fun isNightModeEnabled(): Flow<Boolean> {
    isNightModeEnabledFlow.value =
      preferencesManager.getFromPreferences(NIGHT_MODE_KEY, false) == true
    return isNightModeEnabledFlow
  }

  override fun isCurrentLanguageEnglish(): Boolean {
    return (preferencesManager.getFromPreferences<String>(LANGUAGE_KEY, "ar") ?: "ar") == "en"
  }

  override fun flipNightMode() {
    preferencesManager.addToPreferences(NIGHT_MODE_KEY, !isNightModeEnabledFlow.value)
    isNightModeEnabledFlow.value =
      preferencesManager.getFromPreferences(NIGHT_MODE_KEY, false) == true
  }

  override fun updateCurrentLanguage() {
    val newLocale = if (isCurrentLanguageEnglish()) "ar" else "en"
    preferencesManager.addToPreferences(LANGUAGE_KEY, newLocale)
  }
  //endregion
}