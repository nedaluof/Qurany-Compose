package com.nedaluof.data.repositories.suras

import com.nedaluof.data.model.Reciter
import com.nedaluof.data.model.SuraModel

/**
 * Created by NedaluOf on 8/16/2021.
 */
interface SurasRepository {
  fun checkIfSuraExist(subPath: String): Boolean
  fun getMappedReciterSuras(reciterData: Reciter): List<SuraModel>
}