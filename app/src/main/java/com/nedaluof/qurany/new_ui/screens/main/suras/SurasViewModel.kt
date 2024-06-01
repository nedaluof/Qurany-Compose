package com.nedaluof.qurany.new_ui.screens.main.suras

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

  val reciterName = MutableStateFlow("")
  val reciterSuras = MutableStateFlow<List<SuraModel>>(emptyList())
  val loading = MutableStateFlow(true)

  fun loadSurasToUI(reciterData: Reciter) {
    reciterName.value = reciterData.name ?: ""
    reciterSuras.value = repository.getMappedReciterSuras(reciterData)
    loading.value = false
  }

  fun checkSuraExist(sura: SuraModel): StateFlow<Boolean?> =
    MutableStateFlow<Boolean?>(null).apply {
      value = repository.checkIfSuraExist(sura.suraSubPath)
    }
}