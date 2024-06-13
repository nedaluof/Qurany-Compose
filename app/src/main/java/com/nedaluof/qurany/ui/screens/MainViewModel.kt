package com.nedaluof.qurany.ui.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.nedaluof.data.repositories.app.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by NedaluOf on 12/9/2022.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
  private val appRepository: AppRepository
) : ViewModel() {

  //app language
  val appLanguageEnglish: MutableState<String>
    get() = mutableStateOf(if (appRepository.isCurrentLanguageEnglish()) "العربية" else "EN")

  val isCurrentLanguageEnglish: MutableState<Boolean>
    get() = mutableStateOf(appRepository.isCurrentLanguageEnglish())


  //night mode
  val isNightModeEnabled = mutableStateOf(appRepository.isNightModeEnabled())

  fun changeDayNightMode() {
    val currentMode = appRepository.isNightModeEnabled()
    val newMode = !currentMode
    appRepository.updateNightMode(newMode)
    isNightModeEnabled.value = newMode
  }

  fun changeAppLanguage() {
    appRepository.updateCurrentLanguage()
  }
}