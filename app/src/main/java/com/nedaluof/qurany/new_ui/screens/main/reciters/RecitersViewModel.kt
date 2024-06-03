package com.nedaluof.qurany.new_ui.screens.main.reciters

import androidx.lifecycle.viewModelScope
import com.nedaluof.qurany.data.model.Reciter
import com.nedaluof.qurany.data.model.Status
import com.nedaluof.qurany.data.repository.RecitersRepository
import com.nedaluof.qurany.new_ui.base.BaseViewModel
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
) : BaseViewModel() {

  //region variables
  // reciters list from API or from DB
  val recitersUiState = MutableStateFlow<RecitersUiState>(RecitersUiState.Loading)

  //reciter operations state for delete / add processes
  val recitersOperationUiState =
    MutableStateFlow<RecitersOperationsUiState>(RecitersOperationsUiState.Idl)

  var reciterToBeProcessed: Reciter? = null
  //endregion

  fun loadReciters(
    loadMyReciters: Boolean = false
  ) {
    recitersUiState.value = RecitersUiState.Success(emptyList())
    recitersUiState.value = RecitersUiState.Loading
    viewModelScope.launch(Dispatchers.Default) {
      if (loadMyReciters) {
        repository.getMyReciters().catch { cause ->
          recitersUiState.value = RecitersUiState.Error(cause.message.toString())
        }.collect {
          recitersUiState.value = RecitersUiState.Success(it)
        }
      } else {
        repository.loadReciters { result ->
          when (result.status) {
            Status.SUCCESS -> recitersUiState.value =
              RecitersUiState.Success(result.data ?: emptyList())

            Status.ERROR -> recitersUiState.value = RecitersUiState.Error(result.message ?: "")
          }
        }
      }
    }
  }

  fun processAddOrDeleteFromMyReciters() {
    reciterToBeProcessed?.let { reciter ->
      recitersOperationUiState.value = RecitersOperationsUiState.Loading
      viewModelScope.launch(Dispatchers.IO) {
        if (reciter.inMyReciters) {
          repository.deleteFromMyReciters(reciter) { result ->
            recitersOperationUiState.value =
              if (result.status == Status.SUCCESS) RecitersOperationsUiState.Success(true) else RecitersOperationsUiState.Error(
                result.message ?: ""
              )
            reciterToBeProcessed = null
          }
        } else {
          repository.addReciterToDatabase(reciter) { result ->
            recitersOperationUiState.value =
              if (result.status == Status.SUCCESS) RecitersOperationsUiState.Success(false) else RecitersOperationsUiState.Error(
                result.message ?: ""
              )
            reciterToBeProcessed = null
          }
        }
      }
    }
  }
}