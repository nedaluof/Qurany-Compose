package com.nedaluof.qurany.ui.screens

import androidx.lifecycle.ViewModel
import com.nedaluof.data.repositories.app.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * Created by NedaluOf on 12/9/2022.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
  private val appRepository: AppRepository
) : ViewModel() {

  //region variables
  private val _isCurrentLanguageEnglish = MutableStateFlow(appRepository.isCurrentLanguageEnglish())
  val isCurrentLanguageEnglish = _isCurrentLanguageEnglish.asStateFlow()

  private val _isNightModeEnabled = MutableStateFlow(appRepository.isNightModeEnabled())
  val isNightModeEnabled = _isNightModeEnabled.asStateFlow()
  //endregion

  //region logic
  fun changeDayNightMode() {
    val currentMode = appRepository.isNightModeEnabled()
    val newMode = !currentMode
    appRepository.updateNightMode(newMode)
    _isNightModeEnabled.value = newMode
  }

  fun changeAppLanguage() {
    appRepository.updateCurrentLanguage()
    _isCurrentLanguageEnglish.value = appRepository.isCurrentLanguageEnglish()
  }
  //endregion
}