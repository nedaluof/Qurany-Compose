package com.nedaluof.data.repositories.app

import kotlinx.coroutines.flow.Flow

/**
 * Created by NedaluOf on 12/9/2022.
 */
interface AppRepository {
  fun isNightModeEnabled(): Flow<Boolean>
  fun isNightModeEnabledBlocking(): Boolean
  fun isCurrentLanguageEnglish(): Boolean
  fun flipNightMode()
  fun updateCurrentLanguage()
}