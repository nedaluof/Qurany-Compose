package com.nedaluof.qurany.ui.screens.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nedaluof.qurany.data.repository.AppRepository
import com.nedaluof.qurany.ui.base.BaseViewModel
import com.nedaluof.qurany.util.ConnectivityStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by NedaluOf on 12/9/2022.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
  private val appRepository: AppRepository
) : BaseViewModel() {

  //app language
  val appLanguageEnglish =
    mutableStateOf(if (appRepository.isCurrentLanguageEnglish()) "العربية" else "EN")

  //night mode
  val isNightModeEnabled = mutableStateOf(appRepository.isNightModeEnabled())

  // connectivity status
  val connected = mutableStateOf(true)

  fun changeDayNightMode() {
    val currentMode = appRepository.isNightModeEnabled()
    val newMode = !currentMode
    appRepository.updateNightMode(newMode)
    isNightModeEnabled.value = newMode
  }

  fun loadAppLanguage() {
    appLanguageEnglish.value =
      if (appRepository.isCurrentLanguageEnglish()) "العربية" else "EN"
  }

  fun changeAppLanguage() {
    appRepository.updateCurrentLanguage()
  }

  private fun observeConnectivity() {
    viewModelScope.launch {
      appRepository.observeConnectivity().collect { connectionState ->
        when (connectionState) {
          ConnectivityStatus.CONNECTED -> connected.value = true
          ConnectivityStatus.NOT_CONNECTED -> connected.value = false
        }
      }
    }
  }
}