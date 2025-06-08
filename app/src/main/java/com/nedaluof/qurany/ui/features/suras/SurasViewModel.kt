package com.nedaluof.qurany.ui.features.suras

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.nedaluof.data.model.ReciterModel
import com.nedaluof.data.model.SuraModel
import com.nedaluof.data.repositories.suras.SurasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by nedaluof on 12/16/2020.
 */
@HiltViewModel
class SurasViewModel @Inject constructor(
  private val repository: SurasRepository
) : ViewModel() {

  //region ui states
  val currentPlayingSura = mutableStateOf<SuraModel?>(null)
  //endregion

  //region logic
  fun loadReciterSuras(reciter: ReciterModel) = repository.loadReciterSuras(reciter)
  fun isSuraExistInLocalStorage(suraPath: String) = repository.checkIfSuraExist(suraPath)
  //endregion
}