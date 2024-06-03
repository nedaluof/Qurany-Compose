package com.nedaluof.qurany.new_ui.screens.main.reciters

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nedaluof.qurany.data.model.Reciter
import com.nedaluof.qurany.data.model.Status
import com.nedaluof.qurany.data.repository.RecitersRepository
import com.nedaluof.qurany.new_ui.base.BaseViewModel
import com.nedaluof.qurany.util.ConnectivityStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
  val uiState = MutableStateFlow<RecitersUiState>(RecitersUiState.Loading)

  // connectivity status
  val connected = mutableStateOf(true)

  // result of adding reciter to the My Reciters List
  private val _resultAdd = MutableStateFlow(false)
  val resultOfAddReciter = _resultAdd.asStateFlow()

  private val _resultOfDeletion = MutableStateFlow(false)
  val resultOfDeleteReciter = _resultOfDeletion.asStateFlow()

  var reciterToBeProcessed: Reciter? = null
  //endregion

  fun loadReciters(
    loadMyReciters: Boolean = false
  ) {
    uiState.value = RecitersUiState.Success(emptyList())
    uiState.value = RecitersUiState.Loading
    viewModelScope.launch(Dispatchers.Default) {
      if (loadMyReciters) {
        repository.getMyReciters().catch { cause ->
          uiState.value = RecitersUiState.Error(cause.message.toString())
        }.collect {
          uiState.value = RecitersUiState.Success(it)
        }
      } else {
        repository.loadReciters { result ->
          when (result.status) {
            Status.SUCCESS -> uiState.value = RecitersUiState.Success(result.data ?: emptyList())
            Status.ERROR -> uiState.value = RecitersUiState.Error(result.message ?: "")
          }
        }
      }
    }
  }

  private fun observeConnectivity() {
    viewModelScope.launch(Dispatchers.IO) {
      repository.observeConnectivity().collect { connectionState ->
        when (connectionState) {
          ConnectivityStatus.CONNECTED -> connected.value = true
          ConnectivityStatus.NOT_CONNECTED -> connected.value = false
        }
      }
    }
  }

  fun processAddOrDeleteFromMyReciters() {
    reciterToBeProcessed?.let { reciter ->
      viewModelScope.launch(Dispatchers.IO) {
        if (reciter.inMyReciters) {
          repository.deleteFromMyReciters(reciter) { result ->
            _resultOfDeletion.value = result.status == Status.SUCCESS
            reciterToBeProcessed = null
          }
        } else {
          repository.addReciterToDatabase(reciter) { result ->
            _resultAdd.value = result.status == Status.SUCCESS
            reciterToBeProcessed = null
          }
        }
      }
    }
  }

  init {
    //observeConnectivity()
  }
}