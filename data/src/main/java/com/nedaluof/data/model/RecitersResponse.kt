package com.nedaluof.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import kotlinx.parcelize.Parcelize

/**
 * Created by nedaluof on 12/2/2020.
 */
@JsonClass(generateAdapter = true)
data class RecitersResponse(
  var reciters: List<ReciterDto>? = null
)

@JsonClass(generateAdapter = true)
@Parcelize
data class ReciterDto(
  var id: String? = null,
  var name: String? = null,
  @Json(name = "Server") var server: String? = null,
  var rewaya: String? = null,
  var count: String? = null,
  var letter: String? = null,
  var suras: String? = null,
) : Parcelable

@Entity(tableName = "reciters_table")
data class ReciterEntity(
  @PrimaryKey(autoGenerate = true) var reciterId: Int = 0,
  var id: String? = null,
  var name: LocalText? = null,
  var serverLink: String? = null,
  var rewaya: LocalText? = null,
  var count: String? = null,
  var letter: LocalText? = null,
  var suras: String? = null,
  var isInMyFavorites: Boolean = false
)

fun List<ReciterEntity>.asReciterModels(
  locale: String
) = this.map { entity ->
  ReciterModel().apply {
    id = entity.reciterId
    name = if (locale == "en") entity.name?.english ?: "" else entity.name?.arabic ?: ""
    serverLink = entity.serverLink ?: ""
    rewaya = if (locale == "en") entity.rewaya?.english ?: "" else entity.rewaya?.arabic ?: ""
    count = entity.count ?: "0"
    letter = if (locale == "en") entity.letter?.english ?: "" else entity.letter?.arabic ?: ""
    suras = entity.suras ?: ""
    isInMyFavorites = entity.isInMyFavorites
  }
}

@JsonClass(generateAdapter = true)
data class LocalText(
  val arabic: String,
  val english: String
)

@Parcelize
data class ReciterModel(
  var id: Int = 0,
  var name: String = "",
  var serverLink: String = "",
  var rewaya: String = "",
  var count: String = "",
  var letter: String = "",
  var suras: String = "",
  var isInMyFavorites: Boolean = false
) : Parcelable {
  companion object {
    fun mockList() = listOf(ReciterModel().apply {
      id = 1
      name = "Maher Al-Mueqle"
      rewaya = "Hafs An Asem"
      count = "There 114 Suras"
      letter = ""
      suras = ""
      isInMyFavorites = false
    })
  }
}

class LocalTextTypeConverter {
  private val moshi: Moshi = Moshi.Builder().build()

  @TypeConverter
  fun toString(data: LocalText): String {
    val adapter: JsonAdapter<LocalText> = moshi.adapter(LocalText::class.java)
    return adapter.toJson(data)
  }

  @TypeConverter
  fun fromString(data: String): LocalText? {
    val adapter: JsonAdapter<LocalText> = moshi.adapter(LocalText::class.java)
    return adapter.fromJson(data)
  }
}