package com.nedaluof.qurany.ui.screens.reciters

import androidx.compose.runtime.Stable

/**
 * Created By NedaluOf - 6/1/2024.
 */
@Stable
sealed interface RecitersUiState {
  data object ShowReciter : RecitersUiState
  data class Error(val message: String?) : RecitersUiState
  data object Loading : RecitersUiState
}

@Stable
sealed interface RecitersOperationsUiState {
  data class Success(val isDeleted: Boolean) : RecitersOperationsUiState
  data class Error(val message: String?) : RecitersOperationsUiState
  data object Loading : RecitersOperationsUiState
  data object Idl : RecitersOperationsUiState
}