package com.nedaluof.qurany.ui.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nedaluof.qurany.data.repository.AppRepository
import com.nedaluof.qurany.new_ui.base.BaseViewModel
import com.nedaluof.qurany.util.ConnectivityStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
  val appLanguageEnglish = MutableStateFlow("")

  //night mode
  private val _isNightModeEnabled = MutableStateFlow<Boolean?>(null)
  val isNightModeEnabled = _isNightModeEnabled.asStateFlow()

  // connectivity status
  val connected = mutableStateOf(true)

  fun changeDayNightMode() {
    val currentMode = appRepository.isNightModeEnabled()
    val newMode = !currentMode
    appRepository.updateNightMode(newMode)
    _isNightModeEnabled.value = newMode
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