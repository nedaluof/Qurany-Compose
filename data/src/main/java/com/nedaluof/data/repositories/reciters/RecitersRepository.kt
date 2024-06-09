package com.nedaluof.data.repositories.reciters

import com.nedaluof.data.model.ReciterModel
import com.nedaluof.data.model.Result
import kotlinx.coroutines.flow.Flow

/**
 * Created by NedaluOf on 8/16/2021.
 */
interface RecitersRepository {
  fun loadReciters(): Flow<List<ReciterModel>>

  fun loadFavoriteReciters(): Flow<List<ReciterModel>>

  fun addOrRemoveReciterFromFavorites(
    reciterId: Int,
    isInMyFavorites: Boolean,
    result: (Result<Boolean>) -> Unit
  )
}