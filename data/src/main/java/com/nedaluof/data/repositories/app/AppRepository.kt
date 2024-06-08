package com.nedaluof.data.repositories.app

import com.nedaluof.data.enum.ConnectivityStatus
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
}