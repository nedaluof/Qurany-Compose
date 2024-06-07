package com.nedaluof.qurany.data.repositories.reciters

import com.nedaluof.qurany.data.model.Reciter
import com.nedaluof.qurany.data.model.Result
import kotlinx.coroutines.flow.Flow

/**
 * Created by NedaluOf on 8/16/2021.
 */
interface RecitersRepository {
  suspend fun loadReciters(result: (Result<List<Reciter>>) -> Unit)
  suspend fun addReciterToDatabase(reciter: Reciter, result: (Result<Boolean>) -> Unit)
  fun getMyReciters(): Flow<List<Reciter>>
  suspend fun deleteFromMyReciters(reciter: Reciter, result: (Result<Boolean>) -> Unit)
}