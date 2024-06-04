package com.nedaluof.qurany.data.repositoryImpl

import android.content.Context
import com.nedaluof.qurany.data.model.Reciter
import com.nedaluof.qurany.data.model.SuraModel
import com.nedaluof.qurany.data.repository.SurasRepository
import com.nedaluof.qurany.util.SuraUtil
import com.nedaluof.qurany.util.getLanguage
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

/**
 * Created by nedaluof on 12/11/2020.
 */
class SurasRepositoryImpl @Inject constructor(
  @ApplicationContext
  private val context: Context,
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
    val mappedReciterSuraModels = ArrayList<SuraModel>()
    for (i in currentReciterSuras.indices) {
      val currentSura = currentReciterSuras[i].toInt()
      if (getLanguage() == "_arabic") {
        val suraName = SuraUtil.getArabicSuraName()[currentSura - 1].name
        val suraUrl = reciterData.server + "/" + SuraUtil.getSuraIndex(currentSura) + ".mp3"
        val subPath = "/Qurany/${reciterData.name}/${SuraUtil.getSuraName(currentSura)}.mp3"
        mappedReciterSuraModels.add(
          SuraModel(
            currentSura,
            suraName,
            "رواية : " + reciterData.rewaya,
            suraUrl,
            reciterData.name ?: "",
            SuraUtil.getPlayerTitle(
              currentSura,
              reciterData.name ?: ""
            ),
            subPath
          )
        )
      } else {
        val suraName = SuraUtil.getEnglishSuraName()[currentSura - 1].name
        val suraUrl = reciterData.server + "/" + SuraUtil.getSuraIndex(currentSura) + ".mp3"
        val subPath = "/Qurany/${reciterData.name}/${SuraUtil.getSuraName(currentSura)}.mp3"
        mappedReciterSuraModels.add(
          SuraModel(
            currentSura,
            suraName,
            reciterData.rewaya ?: "",
            suraUrl,
            reciterData.name ?: "",
            SuraUtil.getPlayerTitle(currentSura, reciterData.name ?: ""),
            subPath
          )
        )
      }
    }
    return mappedReciterSuraModels
  }
}