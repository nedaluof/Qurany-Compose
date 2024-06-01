package com.nedaluof.qurany.data.repositoryImpl

import android.content.Context
import com.nedaluof.qurany.data.datasource.localsource.preferences.PreferencesManager
import com.nedaluof.qurany.data.datasource.localsource.room.RecitersDao
import com.nedaluof.qurany.data.datasource.remotesource.api.ApiService
import com.nedaluof.qurany.data.model.Reciter
import com.nedaluof.qurany.data.model.Result
import com.nedaluof.qurany.data.repository.RecitersRepository
import com.nedaluof.qurany.util.connectivityFlow
import com.nedaluof.qurany.util.getLanguage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by nedaluof on 12/11/2020.
 */
@ExperimentalCoroutinesApi
class RecitersRepositoryImpl @Inject constructor(
  private val apiService: ApiService,
  private val preferences: PreferencesManager,
  private val recitersDao: RecitersDao,
  @ApplicationContext private val context: Context
) : RecitersRepository {

  override suspend fun loadReciters(result: (Result<List<Reciter>>) -> Unit) {
    try {
      val response = apiService.getReciters(getLanguage())
      if (response.isSuccessful) {
        response.body()?.reciters?.map { reciter ->
          preferences.hasKeyInPreferences(reciter.id!!) { result ->
            reciter.inMyReciters = result
          }
        }
        result(Result.success(response.body()?.reciters!!))
      } else {
        result(Result.error(null, response.message()))
      }
    } catch (exception: Exception) {
      //result(Result.error(null, exception.message!!))
      //Timber.e(exception)
    }
  }

  override suspend fun addReciterToDatabase(reciter: Reciter, result: (Result<Boolean>) -> Unit) {
    try {
      recitersDao.insertReciter(reciter)
      preferences.addToPreferences(reciter.id!!, reciter.id!!)
      // inform user that reciter added to My Reciters List
      result(Result.success(true))
    } catch (exception: Exception) {
      Timber.d("addReciterToDatabase: Error : ${exception.message} ")
      result(Result.error(null, exception.message!!))
    }
  }

  override suspend fun observeConnectivity() = context.connectivityFlow()

  override fun getMyReciters() = recitersDao.getMyReciters()
    .distinctUntilChanged()

  override suspend fun deleteFromMyReciters(reciter: Reciter, result: (Result<Boolean>) -> Unit) {
    try {
      recitersDao.deleteReciter(reciter)
      // remove from preferences
      preferences.removeFromPreferences(reciter.id!!)
      // now inform  deletion successfully
      result(Result.success(true))
    } catch (exception: Exception) {
      result(Result.error(null, exception.message!!))
    }
  }
}