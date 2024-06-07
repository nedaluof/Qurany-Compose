package com.nedaluof.qurany.data.repositories.app

import android.content.Context
import android.content.pm.PackageManager
import com.nedaluof.qurany.R
import com.nedaluof.qurany.data.datasource.localsource.preferences.PreferencesKeys.LANGUAGE_KEY
import com.nedaluof.qurany.data.datasource.localsource.preferences.PreferencesKeys.NIGHT_MODE_KEY
import com.nedaluof.qurany.data.datasource.localsource.preferences.PreferencesManager
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

  override fun getAppVersionName() = try {
    val versionLabel = context.getString(R.string.app_version)
    val versionName = context.packageManager.getPackageInfo(context.packageName, 0).versionName
    "$versionLabel $versionName"
  } catch (e: PackageManager.NameNotFoundException) {
    e.printStackTrace()
    "1.0"
  }
}