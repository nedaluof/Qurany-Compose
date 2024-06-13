package com.nedaluof.qurany.ui.screens.reciters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nedaluof.data.model.ReciterModel
import com.nedaluof.data.model.Status
import com.nedaluof.data.repositories.reciters.RecitersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
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
  val recitersUiState = MutableStateFlow<RecitersUiState>(RecitersUiState.Loading)
  val recitersOperationUiState =
    MutableStateFlow<RecitersOperationsUiState>(RecitersOperationsUiState.Idl)
  var reciterToBeProcessed: ReciterModel? = null
  //endregion

  fun loadReciters(
    loadFavoriteReciters: Boolean = false
  ) {
    recitersUiState.value = RecitersUiState.Success(emptyList())
    recitersUiState.value = RecitersUiState.Loading
    viewModelScope.launch(Dispatchers.IO) {
      repository.loadReciters(loadFavoriteReciters).catch { cause ->
        recitersUiState.value = RecitersUiState.Error(cause.message.toString())
      }.collect {
        recitersUiState.value = RecitersUiState.Success(it)
      }
    }
  }

  fun processAddOrDeleteFromFavorites() {
    reciterToBeProcessed?.let { reciter ->
      repository.addOrRemoveReciterFromFavorites(
        reciter.id,
        reciter.isInMyFavorites
      ) { result ->
        recitersOperationUiState.value =
          if (result.status == Status.SUCCESS) RecitersOperationsUiState.Success(reciter.isInMyFavorites)
          else RecitersOperationsUiState.Error(
            result.message ?: ""
          )
        reciterToBeProcessed = null
      }
    }
  }
}