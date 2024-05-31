package com.nedaluof.qurany.ui.main.myreciters

import androidx.lifecycle.viewModelScope
import com.nedaluof.qurany.data.model.Reciter
import com.nedaluof.qurany.data.model.Status
import com.nedaluof.qurany.data.repository.MyRecitersRepository
import com.nedaluof.qurany.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by nedaluof on 12/12/2020.
 */
@HiltViewModel
class MyRecitersViewModel @Inject constructor(
  private val repository: MyRecitersRepository,
) : BaseViewModel() {

  val reciters = MutableStateFlow<List<Reciter>>(emptyList())
  val loading = MutableStateFlow(true)
  val noReciters = MutableStateFlow(false)

  private val _error = MutableStateFlow(false)
  val error: StateFlow<Boolean> = _error

  private fun getMyReciters() {
    viewModelScope.launch {
      repository.getMyReciters()
        .catch { cause ->
          _error.value = true
          Timber.e(cause)
        }
        .collect {
          if (it.isNotEmpty()) {
            loading.value = false
            reciters.value = it
          } else {
            reciters.value = emptyList()
            loading.value = false
            noReciters.value = true
          }
        }
    }
  }

  private val _resultOfDeletion = MutableStateFlow(false)
  val resultOfDeleteReciter: StateFlow<Boolean> = _resultOfDeletion

  fun deleteFromMyReciters(reciter: Reciter) {
    viewModelScope.launch {
      repository.deleteFromMyReciters(reciter) { result ->
        when (result.status) {
          Status.SUCCESS -> _resultOfDeletion.value = true
          Status.ERROR -> _resultOfDeletion.value = false
        }
      }
    }
  }

  init {
    getMyReciters()
  }
}