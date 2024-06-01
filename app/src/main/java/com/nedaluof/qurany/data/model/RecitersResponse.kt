package com.nedaluof.qurany.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by nedaluof on 12/2/2020.
 */
data class Reciters(
  @SerializedName("reciters") var reciters: List<Reciter>? = null
)

@Parcelize
@Entity(tableName = "reciter")
data class Reciter(
  @SerializedName("id") var id: String? = null,
  @SerializedName("name") var name: String? = null,
  @SerializedName("Server") var server: String? = null,
  @SerializedName("rewaya") var rewaya: String? = null,
  @SerializedName("count") var count: String? = null,
  @SerializedName("letter") var letter: String? = null,
  @SerializedName("suras") var suras: String? = null,
  var inMyReciters: Boolean = false,
  @PrimaryKey(autoGenerate = true) var reciter_id: Int = 0,
  var isPlayingNow: Boolean = false
) : Parcelable {

  companion object {
    fun mockList() = listOf(Reciter().apply {
      id = "1"
      name = "Maher Al-Mueqle"
      rewaya = "Hafs An Asem"
      count = "There 114 Suras"
      letter = ""
      suras = ""
      inMyReciters = false
    }, Reciter().apply {
      id = "2"
      name = "Maher Al-Mueqle"
      rewaya = "Hafs An Asem"
      count = "There 114 Suras"
      letter = ""
      suras = ""
      inMyReciters = false
    }, Reciter().apply {
      id = "3"
      name = "Maher Al-Mueqle"
      rewaya = "Hafs An Asem"
      count = "There 114 Suras"
      letter = ""
      suras = ""
      inMyReciters = false
    })
  }
}