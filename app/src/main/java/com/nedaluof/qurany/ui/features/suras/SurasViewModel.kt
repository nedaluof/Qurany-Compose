package com.nedaluof.qurany.ui.features.suras

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nedaluof.data.model.ReciterModel
import com.nedaluof.data.model.SuraModel
import com.nedaluof.data.repositories.reciters.RecitersRepository
import com.nedaluof.data.repositories.suras.SurasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by nedaluof on 12/16/2020.
 */
@HiltViewModel
class SurasViewModel @Inject constructor(
  private val surasRepository: SurasRepository,
  private val recitersRepository: RecitersRepository
) : ViewModel() {

  //region variables
  private val _uiState = MutableStateFlow(SurasUiState())
  val uiState = _uiState.asStateFlow()

  private var reciter: ReciterModel? = null
  val currentPlayingSura = mutableStateOf<SuraModel?>(null)
  //endregion

  //region logic
  fun loadReciterSuras(reciterId: Int) {
    viewModelScope.launch {
      recitersRepository.getReciterById(reciterId).collect { result ->
        result.onSuccess { reciterModel ->
          reciter = reciterModel
          _uiState.update {
            it.copy(
              reciterName = reciterModel.name,
              isReciterInFavorites = reciterModel.isInMyFavorites,
              suras = surasRepository.loadReciterSuras(reciterModel)
            )
          }
        }.onFailure { throwable ->
          _uiState.update {
            it.copy(errorMessage = throwable.message)
          }
        }
      }
    }
  }

  fun isSuraExistInLocalStorage(suraPath: String) = surasRepository.checkIfSuraExist(suraPath)

  fun addOrDeleteFromFavorites() {
    reciter?.let { model ->
      viewModelScope.launch {
        recitersRepository.addOrRemoveReciterFromFavorites(model.id)
          .collect { result ->
            if (result.isSuccess) {
              reciter = reciter?.copy(isInMyFavorites = !model.isInMyFavorites)
            }
            _uiState.update {
              it.copy(
                errorMessage = result.exceptionOrNull()?.message,
                isReciterInFavorites = reciter?.isInMyFavorites == true,
                isAddedToFavorites = if (result.isSuccess) !model.isInMyFavorites else false,
                isDeletedFromFavorites = if (result.isSuccess) model.isInMyFavorites else false
              )
            }
            delay(2000)
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
  }
  //endregion
}