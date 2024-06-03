package com.nedaluof.qurany.data.repositoryImpl

import android.content.Context
import com.nedaluof.qurany.data.datasource.localsource.preferences.PreferencesManager
import com.nedaluof.qurany.data.repository.AppRepository
import com.nedaluof.qurany.util.ConnectivityStatus
import com.nedaluof.qurany.util.connectivityFlow
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by NedaluOf on 12/9/2022.
 */
class AppRepositoryImpl @Inject constructor(
  @ApplicationContext private val context: Context,
  private val preferencesManager: PreferencesManager
) : AppRepository {

  @OptIn(ExperimentalCoroutinesApi::class)
  override suspend fun observeConnectivity(): Flow<ConnectivityStatus> = context.connectivityFlow()

  override fun isNightModeEnabled(): Boolean {
    return preferencesManager.getFromPreferences(NIGHT_MODE_KEY, false) ?: false
  }

  override fun isCurrentLanguageEnglish(): Boolean {
    return (preferencesManager.getFromPreferences<String>(LANGUAGE_KEY, "en") ?: "en") == "en"
  }

  override fun updateNightMode(isEnabled: Boolean) {
    preferencesManager.addToPreferences(NIGHT_MODE_KEY, isEnabled)
  }

  override fun updateCurrentLanguage() {
    val newLocale = if (isCurrentLanguageEnglish()) "ar" else "en"
    preferencesManager.addToPreferences(LANGUAGE_KEY, newLocale)
  }

  companion object {
    private const val NIGHT_MODE_KEY = "NIGHT_MODE_KEY"
    private const val LANGUAGE_KEY = "LANGUAGE_KEY"
  }
}