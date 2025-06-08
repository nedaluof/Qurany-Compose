package com.nedaluof.qurany.ui.features.suras

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.nedaluof.data.model.ReciterModel
import com.nedaluof.data.model.SuraModel
import com.nedaluof.data.repositories.reciters.RecitersRepository
import com.nedaluof.data.repositories.suras.SurasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
  private var reciter: ReciterModel? = null
  val currentPlayingSura = mutableStateOf<SuraModel?>(null)
  //endregion

  //region logic
  fun loadReciterSuras(reciter: ReciterModel): List<SuraModel> {
    this.reciter = reciter
    return surasRepository.loadReciterSuras(reciter)
  }

  fun isSuraExistInLocalStorage(suraPath: String) = surasRepository.checkIfSuraExist(suraPath)

  fun processAddOrDeleteFromFavorites() {
    /*reciter?.let { reciter ->
      recitersRepository.addOrRemoveReciterFromFavorites(
        reciter.id, reciter.isInMyFavorites
      ) { result ->
        *//*_recitersOperationUiState.set(
          if (result is DataResult.Success) RecitersOperationsUiState.Success(reciter.isInMyFavorites)
          else RecitersOperationsUiState.Error(result.error),
          RecitersOperationsUiState.Idl
        )*//*
      }
    }*/
  }
  //endregion
}