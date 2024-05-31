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
  var suraName: String = "",
  var rewaya: String = "",
  var suraUrl: String = "",
  var reciterName: String = "",
  var playerTitle: String = "",
  var suraSubPath: String = "",
  var playingType: Int = AppConstants.PLAYING_ONLINE,
) : Parcelable
