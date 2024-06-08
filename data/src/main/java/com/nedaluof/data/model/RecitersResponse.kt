package com.nedaluof.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/**
 * Created by nedaluof on 12/2/2020.
 */
@JsonClass(generateAdapter = true)
data class Reciters(
  var reciters: List<Reciter>? = null
)

@JsonClass(generateAdapter = true)
@Entity(tableName = "reciter")
@Parcelize
data class Reciter(
  var id: String? = null,
  var name: String? = null,
  @Json(name = "Server") var server: String? = null,
  var rewaya: String? = null,
  var count: String? = null,
  var letter: String? = null,
  var suras: String? = null,
  @Json(ignore = true) var inMyReciters: Boolean = false,
  @Json(ignore = true) @PrimaryKey(autoGenerate = true) var reciterId: Int = 0
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