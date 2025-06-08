package com.nedaluof.qurany.ui.features.reciters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nedaluof.data.model.ReciterModel
import com.nedaluof.data.repositories.reciters.RecitersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by nedaluof on 12/11/2020.
 */
@HiltViewModel
class RecitersViewModel @Inject constructor(
  private val repository: RecitersRepository
) : ViewModel() {

  //region variables
  private val _uiState = MutableStateFlow(RecitersUiState())
  val uiState = _uiState.asStateFlow()

  private var isForFavoritesReciters = false
  //endregion

  //region logic
  fun loadReciters(loadFavoriteReciters: Boolean = false) {
    isForFavoritesReciters = loadFavoriteReciters
    _uiState.update {
      it.copy(
        reciters = emptyList(),
        showLoading = true
      )
    }
    viewModelScope.launch(Dispatchers.IO) {
      repository.loadReciters(loadFavoriteReciters) { errorMessage ->
        _uiState.update {
          it.copy(
            errorMessage = errorMessage,
            showLoading = false
          )
        }
      }.collectLatest { recitersList ->
        _uiState.update { state ->
          state.copy(
            errorMessage = null,
            showLoading = false,
            reciters = if (state.searchQuery.isNotEmpty() && state.searchQuery.isNotBlank()) {
              recitersList.filter { reciter ->
                reciter.name.uppercase().contains(state.searchQuery.trim().uppercase())
              }
            } else recitersList
          )
        }
      }
    }
  }

  fun addReciterToFavorites(reciterModel: ReciterModel) {
    processAddOrDeleteFromFavorites(reciterModel)
  }

  fun deleteReciterFromFavorites(reciterModel: ReciterModel) {
    processAddOrDeleteFromFavorites(reciterModel)
  }

  private fun processAddOrDeleteFromFavorites(
    reciterModel: ReciterModel
  ) {
    viewModelScope.launch {
      repository.addOrRemoveReciterFromFavorites(reciterModel.id)
        .collect { result ->
          _uiState.update {
            it.copy(
              errorMessage = result.exceptionOrNull()?.message,
              isAddedToFavorites = if (result.isSuccess) !reciterModel.isInMyFavorites else false,
              isDeletedFromFavorites = if (result.isSuccess) reciterModel.isInMyFavorites else false
            )
          }
          delay(1500)
          _uiState.update {
            it.copy(
              errorMessage = null,
              isAddedToFavorites = false,
              isDeletedFromFavorites = false
            )
          }
        }
    }
  }

  fun onSearchTextChange(text: String) {
    _uiState.update { it.copy(searchQuery = text) }
    loadReciters(isForFavoritesReciters)
  }

  fun toggleSearching() {
    _uiState.update {
      it.copy(
        isSearching = !it.isSearching,
        searchQuery = ""
      )
    }
  }
  //endregion
}