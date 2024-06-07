package com.nedaluof.qurany.data.repositories.app

import com.nedaluof.qurany.util.ConnectivityStatus
import kotlinx.coroutines.flow.Flow

/**
 * Created by NedaluOf on 12/9/2022.
 */
interface AppRepository {
  suspend fun observeConnectivity(): Flow<ConnectivityStatus>

  fun isNightModeEnabled(): Boolean
  fun isCurrentLanguageEnglish(): Boolean

  fun updateNightMode(isEnabled: Boolean)
  fun updateCurrentLanguage()

  fun getAppVersionName(): String
}