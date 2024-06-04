package com.nedaluof.qurany.data.model

import android.os.Parcelable
import com.nedaluof.qurany.util.AppConstants
import kotlinx.parcelize.Parcelize

/**
 * Created by nedaluof on 12/2/2020.
 */
@Parcelize
data class SuraModel(
  var id: Int = 0,
  var name: String = "",
  var rewaya: String = "",
  var suraUrl: String = "",
  var reciterName: String = "",
  var playerTitle: String = "",
  var suraSubPath: String = "",
  var playingType: Int = AppConstants.PLAYING_ONLINE,
) : Parcelable {
  companion object {
    fun mockList() = listOf(
      SuraModel().apply {
        id = 1
        name = "Al Fatiha"
        rewaya = "Hafs An Assem"
        reciterName = "Maher Al Mueqle"
        playerTitle = "Al Fatiha | Maher Al Mueqle"
      },
      SuraModel().apply {
        id = 2
        name = "Al Flaq"
        rewaya = "Hafs An Assem"
        reciterName = "Maher Al Mueqle"
        playerTitle = "Al Flaq | Maher Al Mueqle"
      },
      SuraModel().apply {
        id = 3
        name = "Al Qahf"
        rewaya = "Hafs An Assem"
        reciterName = "Maher Al Mueqle"
        playerTitle = "Al Qahf | Maher Al Mueqle"
      },
    )
  }
}