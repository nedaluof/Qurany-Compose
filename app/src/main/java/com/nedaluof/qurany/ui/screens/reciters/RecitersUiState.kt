package com.nedaluof.qurany.ui.screens.reciters

import androidx.compose.runtime.Stable
import com.nedaluof.data.model.Reciter

/**
 * Created By NedaluOf - 6/1/2024.
 */
@Stable
sealed interface RecitersUiState {
  data class Success(val reciters: List<Reciter>) : RecitersUiState
  data class Error(val message: String) : RecitersUiState
  data object Loading : RecitersUiState
}

@Stable
sealed interface RecitersOperationsUiState {
  data class Success(val isDeleted: Boolean) : RecitersOperationsUiState
  data class Error(val message: String) : RecitersOperationsUiState
  data object Loading : RecitersOperationsUiState
  data object Idl : RecitersOperationsUiState
}