package com.nedaluof.data.repositories.reciters

import com.nedaluof.data.datasource.localsource.preferences.PreferencesKeys
import com.nedaluof.data.datasource.localsource.preferences.PreferencesManager
import com.nedaluof.data.datasource.localsource.room.RecitersDao
import com.nedaluof.data.datasource.remotesource.api.ApiService
import com.nedaluof.data.model.LocalText
import com.nedaluof.data.model.ReciterDto
import com.nedaluof.data.model.ReciterEntity
import com.nedaluof.data.model.ReciterModel
import com.nedaluof.data.model.asReciterModel
import com.nedaluof.data.model.asReciterModels
import com.nedaluof.data.util.catchOnSuspend
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by nedaluof on 12/11/2020.
 */
class RecitersRepositoryImpl @Inject constructor(
  private val apiService: ApiService,
  private val preferences: PreferencesManager,
  private val recitersDao: RecitersDao
) : RecitersRepository {

  //region variables
  private val repositoryCoroutineScope by lazy {
    CoroutineScope(Dispatchers.Default + SupervisorJob())
  }
  //endregion

  //region logic
  override fun loadReciters(
    loadFavoriteReciters: Boolean, onError: (String?) -> Unit
  ): Flow<List<ReciterModel>> {
    val appLanguage = preferences.getFromPreferences(PreferencesKeys.LANGUAGE_KEY, "ar") ?: "ar"
    return if (loadFavoriteReciters) {
      recitersDao.loadFavoriteReciters().map {
        it.asReciterModels(appLanguage)
      }.distinctUntilChanged()
    } else {
      checkDatabaseAndLoad(onError)
      recitersDao.loadReciters().map {
        it.asReciterModels(appLanguage)
      }.distinctUntilChanged()
    }
  }

  override fun getReciterById(reciterId: Int): Flow<Result<ReciterModel>> = flow {
    val appLanguage = preferences.getFromPreferences(PreferencesKeys.LANGUAGE_KEY, "ar") ?: "ar"
    catchOnSuspend({
      val reciter = recitersDao.getReciterById(reciterId)
      emit(Result.success(reciter.asReciterModel(appLanguage)))
    }, {
      emit(Result.failure(Exception(it.message ?: "")))
    })
  }

  override fun addOrRemoveReciterFromFavorites(
    reciterId: Int
  ): Flow<Result<Boolean>> = flow {
    catchOnSuspend({
      val reciter = recitersDao.getReciterById(reciterId)
      recitersDao.updateReciter(!reciter.isInMyFavorites, reciterId)
      emit(Result.success(true))
    }, {
      emit(Result.failure(Exception(it.message ?: "")))
    })
  }

  private fun checkDatabaseAndLoad(
    onError: (String?) -> Unit
  ) {
    repositoryCoroutineScope.launch {
      catchOnSuspend({
        if (recitersDao.loadRecitersCount() == 0) {
          val arabicRecitersVersion = ArrayList<ReciterDto>()
          val englishRecitersVersion = ArrayList<ReciterDto>()
          val finalRecitersVersion = ArrayList<ReciterEntity>()
          val arabicResponse = async { apiService.getReciters("_arabic") }.await()
          val englishResponse = async { apiService.getReciters("_english") }.await()
          if (arabicResponse.isSuccessful) {
            if (englishResponse.isSuccessful) {
              arabicRecitersVersion.addAll(arabicResponse.body()?.reciters ?: emptyList())
              englishRecitersVersion.addAll(englishResponse.body()?.reciters ?: emptyList())
              if (arabicRecitersVersion.isNotEmpty() && englishRecitersVersion.isNotEmpty()) {
                if (arabicRecitersVersion.size == englishRecitersVersion.size) {
                  for (index in arabicRecitersVersion.indices) {
                    finalRecitersVersion.add(
                      ReciterEntity(
                        id = arabicRecitersVersion[index].id, name = LocalText(
                          arabicRecitersVersion[index].name ?: "",
                          englishRecitersVersion[index].name ?: ""
                        ), serverLink = arabicRecitersVersion[index].server, rewaya = LocalText(
                          arabicRecitersVersion[index].rewaya ?: "",
                          englishRecitersVersion[index].rewaya ?: ""
                        ), count = arabicRecitersVersion[index].count, letter = LocalText(
                          arabicRecitersVersion[index].letter ?: "",
                          englishRecitersVersion[index].letter ?: ""
                        ), suras = arabicRecitersVersion[index].suras ?: ""
                      )
                    )
                  }
                  recitersDao.insertReciters(finalRecitersVersion)
                }
              }
            } else {
              onError(englishResponse.errorBody().toString())
            }
          } else {
            onError(arabicResponse.errorBody().toString())
          }
        }
      }, { onError(it.message) })
    }
  }
  //endregion
}