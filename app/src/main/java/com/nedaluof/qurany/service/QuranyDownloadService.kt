package com.nedaluof.qurany.service

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.IBinder
import com.nedaluof.data.model.SuraModel
import com.nedaluof.data.repositories.app.AppRepository
import com.nedaluof.data.repositories.suras.SuraUtil
import com.nedaluof.qurany.R
import com.nedaluof.qurany.util.checkIfSuraExist
import com.nedaluof.qurany.util.getSuraPath
import com.nedaluof.qurany.util.isNetworkOk
import com.nedaluof.qurany.util.parcelable
import com.nedaluof.qurany.util.toast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File
import javax.inject.Inject

/**
 * Created by nedaluof on 12/29/2020.
 */
@AndroidEntryPoint
class QuranyDownloadService : Service() {

  @Inject
  lateinit var appRepository: AppRepository

  // unique id for the being sura downloaded
  var downloadId: Long = 0
  private lateinit var sura: SuraModel
  private lateinit var subPath: String
  private val appLanguage by lazy { if (appRepository.isCurrentLanguageEnglish()) "en" else "ar" }
  override fun onBind(intent: Intent?): IBinder? = null

  @SuppressLint("UnspecifiedRegisterReceiverFlag")
  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    sura = intent?.parcelable(DOWNLOAD_SURA_KEY)!!
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      registerReceiver(
        onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), RECEIVER_EXPORTED
      )
    } else {
      registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }
    startDownload()
    return START_NOT_STICKY
  }

  private fun startDownload() {
    if (this::sura.isInitialized) {
      subPath = "/Qurany/${sura.reciterName}/${SuraUtil.getSuraName(appLanguage, sura.id)}.mp3"
      if (!this.checkIfSuraExist(subPath)) {
        if (this.isNetworkOk()) {
          toast(R.string.alrt_download_start_title)
          val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
          val request = DownloadManager.Request(Uri.parse(sura.suraUrl))
          request.setAllowedNetworkTypes(
            DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE
          ).setAllowedOverRoaming(false).setTitle(SuraUtil.getSuraName(appLanguage, sura.id))
            .setDescription(SuraUtil.getSuraName(appLanguage, sura.id) + "| " + sura.reciterName)
            .setDestinationInExternalFilesDir(this, null, subPath)
          downloadId = downloadManager.enqueue(request)
        } else {
          toast(R.string.alrt_no_internet_msg)
          stopSelf()
        }
      } else {
        toast(R.string.alrt_sura_exist_message)
        stopSelf()
      }
    } else {
      Timber.d("startDownload: sura not initialized")
      stopSelf()
    }
  }

  private val onComplete: BroadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
      val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
      // Checking if the received broadcast is for our enqueued download by matching download id
      if (downloadId == id) {
        toast(R.string.alrt_download_completed_msg)
        scan()
      } else {
        Timber.d("onReceive: download id not match")
      }
    }
  }

  fun scan() {
    val file = File(this.getSuraPath(subPath))
    MediaScannerConnection.scanFile(
      this, arrayOf(file.toString()), arrayOf(file.name), null
    )
  }

  override fun onDestroy() {
    unregisterReceiver(onComplete)
    super.onDestroy()
  }

  companion object {
    private const val DOWNLOAD_SURA_KEY = "DOWNLOAD_SURA_KEY"

    fun getIntent(
      context: Context, suraModel: SuraModel
    ) = Intent(context, QuranyDownloadService::class.java).putExtra(DOWNLOAD_SURA_KEY, suraModel)
  }
}