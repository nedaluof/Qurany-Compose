package com.nedaluof.qurany.data.repositories.suras

import android.content.Context
import com.nedaluof.qurany.data.datasource.localsource.preferences.PreferencesKeys
import com.nedaluof.qurany.data.datasource.localsource.preferences.PreferencesManager
import com.nedaluof.qurany.data.model.Reciter
import com.nedaluof.qurany.data.model.SuraModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

/**
 * Created by nedaluof on 12/11/2020.
 */
class SurasRepositoryImpl @Inject constructor(
  @ApplicationContext private val context: Context,
  private val preferences: PreferencesManager,
) : SurasRepository {

  override fun checkIfSuraExist(subPath: String) =
    File(context.getExternalFilesDir(null).toString() + subPath).exists()

  /**
   * Todo issue:
   *  only show language as user device and handle operations
   *  of save and play in english language
   * */
  override fun getMappedReciterSuras(reciterData: Reciter): List<SuraModel> {
    val currentReciterSuras = listOf(
      *reciterData.suras!!.split(regex = "\\s*,\\s*".toRegex())
        .toTypedArray()
    )
    val appLanguage = preferences.getFromPreferences<String?>(PreferencesKeys.LANGUAGE_KEY) ?: "ar"
    val mappedReciterSuraModels = ArrayList<SuraModel>()
    for (i in currentReciterSuras.indices) {
      val currentSura = currentReciterSuras[i].toInt()
      if (appLanguage == "ar") {
        val suraName = SuraUtil.getArabicSuraName()[currentSura - 1].name
        val suraUrl = reciterData.server + "/" + SuraUtil.getSuraIndex(currentSura) + ".mp3"
        val subPath =
          "/Qurany/${reciterData.name}/${SuraUtil.getSuraName(appLanguage, currentSura)}.mp3"
        mappedReciterSuraModels.add(
          SuraModel(
            currentSura,
            suraName,
            "رواية : " + reciterData.rewaya,
            suraUrl,
            reciterData.name ?: "",
            SuraUtil.getPlayerTitle(
              appLanguage,
              currentSura,
              reciterData.name ?: ""
            ),
            subPath,
            checkIfSuraExist(subPath)
          )
        )
      } else {
        val suraName = SuraUtil.getEnglishSuraName()[currentSura - 1].name
        val suraUrl = reciterData.server + "/" + SuraUtil.getSuraIndex(currentSura) + ".mp3"
        val subPath =
          "/Qurany/${reciterData.name}/${SuraUtil.getSuraName(appLanguage, currentSura)}.mp3"
        mappedReciterSuraModels.add(
          SuraModel(
            currentSura,
            suraName,
            reciterData.rewaya ?: "",
            suraUrl,
            reciterData.name ?: "",
            SuraUtil.getPlayerTitle(appLanguage, currentSura, reciterData.name ?: ""),
            subPath,
            checkIfSuraExist(subPath)
          )
        )
      }
    }
    return mappedReciterSuraModels
  }
}