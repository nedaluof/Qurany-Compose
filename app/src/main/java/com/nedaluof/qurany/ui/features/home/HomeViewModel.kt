package com.nedaluof.qurany.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nedaluof.data.repositories.app.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Created By NedaluOf - 08/06/2025.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
  private val appRepository: AppRepository
) : ViewModel() {

  //region variables
  private val _isCurrentLanguageEnglish = MutableStateFlow(appRepository.isCurrentLanguageEnglish())
  val isCurrentLanguageEnglish = _isCurrentLanguageEnglish.asStateFlow()

  val isNightModeEnabled = appRepository.isNightModeEnabled()
    .stateIn(
      scope = viewModelScope,
      initialValue = false,
      started = SharingStarted.WhileSubscribed(5000)
    )
  //endregion

  //region logic
  fun changeDayNightMode() {
    appRepository.flipNightMode()
  }

  fun changeAppLanguage() {
    appRepository.updateCurrentLanguage()
    _isCurrentLanguageEnglish.value = appRepository.isCurrentLanguageEnglish()
  }
  //endregion
}