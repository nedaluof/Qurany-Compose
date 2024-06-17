package com.nedaluof.data.repositories.suras

import android.content.Context
import com.nedaluof.data.datasource.localsource.preferences.PreferencesKeys
import com.nedaluof.data.datasource.localsource.preferences.PreferencesManager
import com.nedaluof.data.model.ReciterModel
import com.nedaluof.data.model.SuraModel
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

  override fun loadReciterSuras(
    reciter: ReciterModel
  ): List<SuraModel> {
    val appLanguage = preferences.getFromPreferences(PreferencesKeys.LANGUAGE_KEY, "ar") ?: "ar"
    val currentReciterSuras = listOf(
      *reciter.suras.split(regex = "\\s*,\\s*".toRegex()).toTypedArray()
    )
    val mappedReciterSuraModels = ArrayList<SuraModel>()
    for (i in currentReciterSuras.indices) {
      val currentSura = currentReciterSuras[i].toInt()
      val suraName = if (appLanguage == "ar") {
        getArabicSuraName()[currentSura - 1].name
      } else {
        getEnglishSuraName()[currentSura - 1].name
      }
      val subPath =
        "/Qurany/${reciter.name}/${getEnglishSuraName()[currentSura - 1].name}.mp3".replace(
          " ",
          "_"
        )
      mappedReciterSuraModels.add(
        SuraModel(
          currentSura,
          suraName,
          reciter.rewaya,
          reciter.serverLink + "/" + getSuraIndex(currentSura) + ".mp3",
          reciter.name,
          reciter.name + " | " + suraName,
          subPath,
          if (checkIfSuraExist(subPath)) File(
            context.getExternalFilesDir(null).toString() + subPath
          ).absolutePath else ""
        )
      )
    }
    return mappedReciterSuraModels
  }

  private fun getArabicSuraName() = listOf(
    SuraModel(1, " الفاتحة"),
    SuraModel(2, "البقرة"),
    SuraModel(3, "ال عمران "),
    SuraModel(4, "النساء"),
    SuraModel(5, " المائدة"),
    SuraModel(6, " الانعام"),
    SuraModel(7, " الأعراف"),
    SuraModel(8, " الأنفال"),
    SuraModel(9, " التوبة "),
    SuraModel(10, " يونس"),
    SuraModel(11, " هود"),
    SuraModel(12, " يوسف"),
    SuraModel(13, " الرعد"),
    SuraModel(14, " إبراهيم"),
    SuraModel(15, " الحجر"),
    SuraModel(16, " النحل"),
    SuraModel(17, " الإسراء"),
    SuraModel(18, " الكهف"),
    SuraModel(19, " مريم"),
    SuraModel(20, " طه"),
    SuraModel(21, " الأنبياء"),
    SuraModel(22, " الحج"),
    SuraModel(23, " المؤمنون"),
    SuraModel(24, " النّور"),
    SuraModel(25, "  الفرقان "),
    SuraModel(26, "  الشعراء "),
    SuraModel(27, " النّمل"),
    SuraModel(28, " القصص"),
    SuraModel(29, " العنكبوت"),
    SuraModel(30, " الرّوم"),
    SuraModel(31, " لقمان"),
    SuraModel(32, " السجدة"),
    SuraModel(33, " الأحزاب"),
    SuraModel(34, " سبأ"),
    SuraModel(35, " فاطر"),
    SuraModel(36, " يس"),
    SuraModel(37, " الصافات"),
    SuraModel(38, " ص"),
    SuraModel(39, " الزمر"),
    SuraModel(40, " غافر"),
    SuraModel(41, " فصّلت"),
    SuraModel(42, " الشورى"),
    SuraModel(43, " الزخرف"),
    SuraModel(44, " الدّخان"),
    SuraModel(45, " الجاثية"),
    SuraModel(46, " الأحقاف"),
    SuraModel(47, " محمد"),
    SuraModel(48, " الفتح"),
    SuraModel(49, " الحجرات"),
    SuraModel(50, " ق"),
    SuraModel(51, " الذاريات"),
    SuraModel(52, " الطور"),
    SuraModel(53, " النجم"),
    SuraModel(54, " القمر"),
    SuraModel(55, " الرحمن"),
    SuraModel(56, " الواقعة"),
    SuraModel(57, " الحديد"),
    SuraModel(58, " المجادلة"),
    SuraModel(59, " الحشر"),
    SuraModel(60, " الممتحنة"),
    SuraModel(61, " الصف"),
    SuraModel(62, " الجمعة"),
    SuraModel(63, " المنافقون"),
    SuraModel(64, " التغابن"),
    SuraModel(65, " الطلاق"),
    SuraModel(66, " التحريم"),
    SuraModel(67, " الملك"),
    SuraModel(68, " القلم"),
    SuraModel(69, " الحاقة"),
    SuraModel(70, " المعارج"),
    SuraModel(71, " نوح"),
    SuraModel(72, " الجن"),
    SuraModel(73, " المزّمّل"),
    SuraModel(74, " المدّثر"),
    SuraModel(75, " القيامة"),
    SuraModel(76, " الإنسان"),
    SuraModel(77, " المرسلات"),
    SuraModel(78, " النبأ"),
    SuraModel(79, " النازعات"),
    SuraModel(80, " عبس"),
    SuraModel(81, " التكوير"),
    SuraModel(82, " الإنفطار"),
    SuraModel(83, " المطفّفين"),
    SuraModel(84, " الإنشقاق"),
    SuraModel(85, " البروج"),
    SuraModel(86, " الطارق"),
    SuraModel(87, " الأعلى"),
    SuraModel(88, " الغاشية"),
    SuraModel(89, " الفجر"),
    SuraModel(90, " البلد"),
    SuraModel(91, " الشمس"),
    SuraModel(92, " الليل"),
    SuraModel(93, " الضحى"),
    SuraModel(94, " الشرح"),
    SuraModel(95, " التين"),
    SuraModel(96, " العلق"),
    SuraModel(97, " القدر"),
    SuraModel(98, " البينة"),
    SuraModel(99, " الزلزلة"),
    SuraModel(100, " العاديات"),
    SuraModel(101, " القارعة"),
    SuraModel(102, " التكاثر"),
    SuraModel(103, " العصر"),
    SuraModel(104, " الهمزة"),
    SuraModel(105, " الفيل"),
    SuraModel(106, " قريش"),
    SuraModel(107, " الماعون"),
    SuraModel(108, " الكوثر"),
    SuraModel(109, " الكافرون"),
    SuraModel(110, " النصر"),
    SuraModel(111, " المسد"),
    SuraModel(112, " الإخلاص"),
    SuraModel(113, " الفلق"),
    SuraModel(114, " النّاس")
  )

  private fun getEnglishSuraName() = listOf(
    SuraModel(1, "Al-Fatihah "),
    SuraModel(2, "Al-Baqarah "),
    SuraModel(3, "Al-'Imran "),
    SuraModel(4, "An-Nisa' "),
    SuraModel(5, "Al-Ma'idah "),
    SuraModel(6, "Al-An'am "),
    SuraModel(7, "Al-A'raf "),
    SuraModel(8, "Al-Anfal "),
    SuraModel(9, "Al-Tawba"),
    SuraModel(10, "Yunus"),
    SuraModel(11, " Hud(Hud)"),
    SuraModel(12, " Yusuf "),
    SuraModel(13, "Ar - Ra'd"),
    SuraModel(14, "Ibrahim"),
    SuraModel(15, " Al - Hijr"),
    SuraModel(16, " An - Nahl"),
    SuraModel(17, " Al-Israa"),
    SuraModel(18, " Al-Kahf  "),
    SuraModel(19, "Maryam "),
    SuraModel(20, "Ta Ha"),
    SuraModel(21, "Al-Anbiya' "),
    SuraModel(22, "Al-Hajj "),
    SuraModel(23, "Al-Mu'minun"),
    SuraModel(24, "An-Nur "),
    SuraModel(25, "Al-Furqan "),
    SuraModel(26, "Ash-Shu'ara'"),
    SuraModel(27, "An-Naml "),
    SuraModel(28, "Al-Qasas "),
    SuraModel(29, "Al-'Ankabut "),
    SuraModel(30, "Ar-Rum "),
    SuraModel(31, "Luqman "),
    SuraModel(32, "As-Sajdah "),
    SuraModel(33, "Al-Ahzab "),
    SuraModel(34, "Al-Saba'"),
    SuraModel(35, "Al-Fatir"),
    SuraModel(36, "Ya Sin "),
    SuraModel(37, "As-Saffat"),
    SuraModel(38, "Sad "),
    SuraModel(39, "Az-Zumar "),
    SuraModel(40, "Ghaffer"),
    SuraModel(41, "Fusilat"),
    SuraModel(42, "Ash-Shura "),
    SuraModel(43, "Az-Zukhruf "),
    SuraModel(44, "Ad-Dukhan"),
    SuraModel(45, "Al-Jathiyah"),
    SuraModel(46, "Al-Ahqaf "),
    SuraModel(47, "Muhammad "),
    SuraModel(48, "Al-Fath "),
    SuraModel(49, "Al-Hujurat "),
    SuraModel(50, "Qaf  "),
    SuraModel(51, "Ad-Dhariyat"),
    SuraModel(52, "At-Tur "),
    SuraModel(53, "An-Najm "),
    SuraModel(54, "Al-Qamar "),
    SuraModel(55, "Ar-Rahman "),
    SuraModel(56, "Al-Waqi'ah"),
    SuraModel(57, "Al-Hadid "),
    SuraModel(58, "Al-Mujadilah"),
    SuraModel(59, "Al-Hashr "),
    SuraModel(60, "Al-Mumtahanah "),
    SuraModel(61, "As-Saff "),
    SuraModel(62, "Al-Jumu'ah "),
    SuraModel(63, "Al-Munafiqun "),
    SuraModel(64, "At-Taghabun "),
    SuraModel(65, "At-Talaq "),
    SuraModel(66, "At-Tahrim "),
    SuraModel(67, "Al-Mulk "),
    SuraModel(68, "Al-Qalam "),
    SuraModel(69, "Al-Haqqah "),
    SuraModel(70, "Al-Ma'arij "),
    SuraModel(71, "Nuh"),
    SuraModel(72, "Al-Jinn "),
    SuraModel(73, "Al-Muzzammil"),
    SuraModel(74, "Al-Muddaththir "),
    SuraModel(75, "Al-Qiyamah "),
    SuraModel(76, "Al-Insan "),
    SuraModel(77, "Al-Mursalat "),
    SuraModel(78, "An-Naba'  "),
    SuraModel(79, "An-Nazi'at "),
    SuraModel(80, "'Abasa"),
    SuraModel(81, "At-Takwir"),
    SuraModel(82, "Al-Infitar "),
    SuraModel(83, "At-Tatfif "),
    SuraModel(84, "Al-Inshiqaq "),
    SuraModel(85, "Al-Buruj "),
    SuraModel(86, "At-Tariq "),
    SuraModel(87, "Al-A'la "),
    SuraModel(88, "Al-Ghashiyah "),
    SuraModel(89, "Al-Fajr "),
    SuraModel(90, "Al-Balad "),
    SuraModel(91, "Ash-Shams "),
    SuraModel(92, "Al-Lail "),
    SuraModel(93, "Ad-Duha "),
    SuraModel(94, "Al-Inshirah "),
    SuraModel(95, "At-Tin "),
    SuraModel(96, "Al-'Alaq  "),
    SuraModel(97, " Al-Qadr "),
    SuraModel(98, " Al-Bayyinah"),
    SuraModel(99, " Al-Zilzal  "),
    SuraModel(100, " Al-'Adiyat "),
    SuraModel(101, " Al-Qari'ah "),
    SuraModel(102, "At-Takathur "),
    SuraModel(103, "Al-'Asr "),
    SuraModel(104, "Al-Humazah "),
    SuraModel(105, "Al-Fil "),
    SuraModel(106, "Al-Quraish "),
    SuraModel(107, "Al-Ma'un  "),
    SuraModel(108, "Al-Kauthar "),
    SuraModel(109, "Al-Kafirun "),
    SuraModel(110, "An-Nasr "),
    SuraModel(111, " Al-Lahab "),
    SuraModel(112, " Al-Ikhlas "),
    SuraModel(113, "Al-Falaq "),
    SuraModel(114, " An-Nas  ")
  )

  private fun getSuraIndex(id: Int) = when {
    id < 9 -> "00$id"
    id in 10..99 -> "0$id"
    else -> id.toString()
  }
}