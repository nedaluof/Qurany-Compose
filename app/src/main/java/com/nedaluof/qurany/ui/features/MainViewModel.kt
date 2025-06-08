package com.nedaluof.qurany.ui.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nedaluof.data.repositories.app.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Created by NedaluOf on 12/9/2022.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
  appRepository: AppRepository
) : ViewModel() {

  //region variables
  val isNightModeEnabled = appRepository.isNightModeEnabled()
    .stateIn(
      scope = viewModelScope,
      initialValue = false,
      started = SharingStarted.WhileSubscribed(5000)
    )
  //endregion
}