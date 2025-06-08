package com.nedaluof.data.repositories.reciters

import com.nedaluof.data.model.ReciterModel
import kotlinx.coroutines.flow.Flow

/**
 * Created by NedaluOf on 8/16/2021.
 */
interface RecitersRepository {

  fun loadReciters(
    loadFavoriteReciters: Boolean = false,
    onError: (String?) -> Unit
  ): Flow<List<ReciterModel>>

  fun addOrRemoveReciterFromFavorites(reciterId: Int): Flow<Result<Boolean>>
}