package com.nedaluof.qurany.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nedaluof.qurany.data.repositories.app.AppRepository
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
) : ViewModel() {

  //app language
  val appLanguageEnglish =
    mutableStateOf(if (appRepository.isCurrentLanguageEnglish()) "العربية" else "EN")

  //splash shown
  val splashScreenShown = mutableStateOf(false)

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
    appLanguageEnglish.value = if (appRepository.isCurrentLanguageEnglish()) "العربية" else "EN"
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