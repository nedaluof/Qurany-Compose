package com.nedaluof.qurany.ui.main

import com.nedaluof.qurany.data.repository.SettingsRepository
import com.nedaluof.qurany.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * Created by NedaluOf on 12/9/2022.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
  private val settingsRepository: SettingsRepository
) : BaseViewModel() {

  //app language
  val appLanguageEnglish = MutableStateFlow("")

  //night mode
  private val _isNightModeEnabled = MutableStateFlow<Boolean?>(null)
  val isNightModeEnabled = _isNightModeEnabled.asStateFlow()

  fun changeDayNightMode() {
    val currentMode = settingsRepository.isNightModeEnabled()
    val newMode = !currentMode
    settingsRepository.updateNightMode(newMode)
    _isNightModeEnabled.value = newMode
  }

  fun loadAppLanguage() {
    appLanguageEnglish.value =
      if (settingsRepository.isCurrentLanguageEnglish()) "العربية" else "EN"
  }

  fun changeAppLanguage() {
    settingsRepository.updateCurrentLanguage()
  }
}