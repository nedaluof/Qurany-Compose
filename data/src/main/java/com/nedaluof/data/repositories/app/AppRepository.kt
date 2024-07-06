package com.nedaluof.data.repositories.app

/**
 * Created by NedaluOf on 12/9/2022.
 */
interface AppRepository {
  fun isNightModeEnabled(): Boolean
  fun isCurrentLanguageEnglish(): Boolean

  fun updateNightMode(isEnabled: Boolean)
  fun updateCurrentLanguage()
}