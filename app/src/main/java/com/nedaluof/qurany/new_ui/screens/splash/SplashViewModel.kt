package com.nedaluof.qurany.new_ui.screens.splash

import androidx.lifecycle.ViewModel
import com.nedaluof.qurany.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * Created by NedaluOf on 9/11/2021.
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
  settingsRepository: SettingsRepository
) : ViewModel() {

  //region ui variables
  private val _isNightModeEnabled = MutableStateFlow(settingsRepository.isNightModeEnabled())
  val isNightModeEnabled = _isNightModeEnabled.asStateFlow()
  //endregion
}