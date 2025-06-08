package com.nedaluof.qurany.ui.features.reciters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nedaluof.data.model.DataResult
import com.nedaluof.data.model.ReciterModel
import com.nedaluof.data.repositories.reciters.RecitersRepository
import com.nedaluof.qurany.util.set
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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
  private val _recitersUiState = MutableStateFlow<RecitersUiState>(RecitersUiState.Loading)
  val recitersUiState = _recitersUiState.asStateFlow()

  private val _recitersOperationUiState =
    MutableStateFlow<RecitersOperationsUiState>(RecitersOperationsUiState.Idl)
  val recitersOperationUiState = _recitersOperationUiState.asStateFlow()

  private val _isSearching = MutableStateFlow(false)
  val isSearching = _isSearching.asStateFlow()

  private val _searchText = MutableStateFlow("")
  val searchText = _searchText.asStateFlow()

  private val _recitersList = MutableStateFlow<List<ReciterModel>?>(null)
  val recitersList = searchText.combine(_recitersList) { text, reciters ->
    reciters?.filter { reciter ->
      reciter.name.uppercase().contains(text.trim().uppercase())
    }
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = _recitersList.value
  )

  var reciterToBeProcessed: ReciterModel? = null
  //endregion

  //region logic
  fun loadReciters(
    loadFavoriteReciters: Boolean = false
  ) {
    _recitersList.value = emptyList()
    _recitersUiState.value = RecitersUiState.Loading
    viewModelScope.launch(Dispatchers.IO) {
      repository.loadReciters(loadFavoriteReciters) { errorMessage ->
        _recitersUiState.value = RecitersUiState.Error(errorMessage)
      }.catch { cause ->
        _recitersUiState.value = RecitersUiState.Error(cause.message.toString())
      }.collectLatest {
        _recitersUiState.value = RecitersUiState.ShowReciter
        _recitersList.value = it
      }
    }
  }

  fun processAddOrDeleteFromFavorites() {
    reciterToBeProcessed?.let { reciter ->
      repository.addOrRemoveReciterFromFavorites(
        reciter.id, reciter.isInMyFavorites
      ) { result ->
        _recitersOperationUiState.set(
          if (result is DataResult.Success) RecitersOperationsUiState.Success(reciter.isInMyFavorites)
          else RecitersOperationsUiState.Error(result.error),
          RecitersOperationsUiState.Idl
        )
        reciterToBeProcessed = null
      }
    }
  }

  fun onSearchTextChange(text: String) {
    _searchText.value = text
  }

  fun toggleSearching() {
    _isSearching.value = !_isSearching.value
    _searchText.value = ""
  }
  //endregion
}