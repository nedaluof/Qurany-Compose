package com.nedaluof.qurany.data.repository

/**
 * Created by NedaluOf on 12/9/2022.
 */
interface SettingsRepository {

  fun isNightModeEnabled(): Boolean
  fun isCurrentLanguageEnglish(): Boolean

  fun updateNightMode(isEnabled: Boolean)
  fun updateCurrentLanguage()
}