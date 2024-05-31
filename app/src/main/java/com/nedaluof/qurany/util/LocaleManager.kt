package com.nedaluof.qurany.util

import android.content.Context
import com.nedaluof.qurany.data.repository.SettingsRepository
import java.util.Locale
import javax.inject.Inject

/**
 * Created by NedaluOf on 12/16/2022.
 */
class LocaleManager @Inject constructor(
  private val settingsRepository: SettingsRepository
) {

  fun configureAppLocale(context: Context): Context {
    return configureAppResources(context)
  }

  private fun configureAppResources(context: Context): Context {
    val locale = Locale(getLanguage())
    Locale.setDefault(locale)
    return updateResources(context, locale)
  }

  private fun updateResources(context: Context, locale: Locale): Context {
    val configuration = context.resources.configuration.apply {
      setLocale(locale)
      setLayoutDirection(locale)
    }
    return context.createConfigurationContext(configuration)
  }

  private fun getLanguage() = if (settingsRepository.isCurrentLanguageEnglish()) "en" else "ar"
}