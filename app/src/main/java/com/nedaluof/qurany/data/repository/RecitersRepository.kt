package com.nedaluof.qurany.data.repository

import com.nedaluof.qurany.data.model.Reciter
import com.nedaluof.qurany.data.model.Result
import com.nedaluof.qurany.util.ConnectivityStatus
import kotlinx.coroutines.flow.Flow

/**
 * Created by NedaluOf on 8/16/2021.
 */
interface RecitersRepository {
  suspend fun loadReciters(result: (Result<List<Reciter>>) -> Unit)
  suspend fun addReciterToDatabase(reciter: Reciter, result: (Result<Boolean>) -> Unit)
  suspend fun observeConnectivity(): Flow<ConnectivityStatus>
}