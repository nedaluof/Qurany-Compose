package com.nedaluof.qurany.data.repositories.reciters

import com.nedaluof.qurany.data.datasource.localsource.preferences.PreferencesKeys
import com.nedaluof.qurany.data.datasource.localsource.preferences.PreferencesManager
import com.nedaluof.qurany.data.datasource.localsource.room.RecitersDao
import com.nedaluof.qurany.data.datasource.remotesource.api.ApiService
import com.nedaluof.qurany.data.model.Reciter
import com.nedaluof.qurany.data.model.Result
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
  private val recitersDao: RecitersDao
) : RecitersRepository {

  override suspend fun loadReciters(result: (Result<List<Reciter>>) -> Unit) {
    try {
      val appLanguage =
        preferences.getFromPreferences<String?>(PreferencesKeys.LANGUAGE_KEY) ?: "ar"
      val response = apiService.getReciters(if (appLanguage == "ar") "_arabic" else "_english")
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