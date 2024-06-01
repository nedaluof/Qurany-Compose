package com.nedaluof.qurany.new_ui.screens.main.reciters

import androidx.compose.runtime.Stable
import com.nedaluof.qurany.data.model.Reciter

/**
 * Created By NedaluOf - 6/1/2024.
 */
@Stable
sealed interface RecitersUiState {
  data class Success(val reciters: List<Reciter>) : RecitersUiState
  data class Error(val message: String) : RecitersUiState
  data object Loading : RecitersUiState
}