package com.nedaluof.qurany.ui.screens.main.suras

import androidx.compose.runtime.mutableStateOf
import com.nedaluof.qurany.data.model.Reciter
import com.nedaluof.qurany.data.model.SuraModel
import com.nedaluof.qurany.data.repository.SurasRepository
import com.nedaluof.qurany.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * Created by nedaluof on 12/16/2020.
 */
@HiltViewModel
class SurasViewModel @Inject constructor(
  private val repository: SurasRepository
) : BaseViewModel() {

  //region ui states
  val playerSheetVisibility = mutableStateOf(true)

  //endregion
  fun loadReciterSuras(reciter: Reciter) = repository.getMappedReciterSuras(reciter)

  fun checkSuraExist(sura: SuraModel): StateFlow<Boolean?> =
    MutableStateFlow<Boolean?>(null).apply {
      value = repository.checkIfSuraExist(sura.suraSubPath)
    }
}