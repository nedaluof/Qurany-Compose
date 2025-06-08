package com.nedaluof.qurany.ui.features.reciters

import androidx.compose.runtime.Stable
import com.nedaluof.data.model.ReciterModel

/**
 * Created By NedaluOf - 6/1/2024.
 */
@Stable
data class RecitersUiState(
  val showLoading: Boolean = false,
  val errorMessage: String? = null,
  val reciters: List<ReciterModel>? = null,
  val isDeletedFromFavorites: Boolean? = null,
  val isAddedToFavorites: Boolean? = null,
  val isSearching: Boolean = false,
  val searchQuery: String = ""
)