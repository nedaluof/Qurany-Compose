package com.nedaluof.qurany.ui.features.suras

import androidx.compose.runtime.Stable
import com.nedaluof.data.model.SuraModel

/**
 * Created By NedaluOf - 08/06/2025.
 */
@Stable
data class SurasUiState(
  val reciterName: String = "",
  val isReciterInFavorites: Boolean = false,
  val errorMessage: String? = null,
  val suras: List<SuraModel> = emptyList(),
  val isDeletedFromFavorites: Boolean? = null,
  val isAddedToFavorites: Boolean? = null,
)
