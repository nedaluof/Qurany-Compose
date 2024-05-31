package com.nedaluof.qurany.service

import android.app.DownloadManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.IBinder
import com.nedaluof.qurany.R
import com.nedaluof.qurany.data.model.SuraModel
import com.nedaluof.qurany.util.AppConstants
import com.nedaluof.qurany.util.SuraUtil
import com.nedaluof.qurany.util.checkIfSuraExist
import com.nedaluof.qurany.util.getParcelableExtraT
import com.nedaluof.qurany.util.getSuraPath
import com.nedaluof.qurany.util.isNetworkOk
import com.nedaluof.qurany.util.toastyError
import com.nedaluof.qurany.util.toastyInfo
import com.nedaluof.qurany.util.toastySuccess
import timber.log.Timber
import java.io.File

/**
 * Created by nedaluof on 12/29/2020.
 */
class QuranyDownloadService : Service() {

  // unique id for the being sura downloaded
  var downloadId: Long = 0
  private lateinit var sura: SuraModel
  private lateinit var subPath: String
  override fun onBind(intent: Intent?): IBinder? = null

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    sura = intent?.getParcelableExtraT(AppConstants.DOWNLOAD_SURA_KEY)!!
    registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    startDownload()
    return START_NOT_STICKY
  }

  private fun startDownload() {
    if (this::sura.isInitialized) {
      subPath = "/Qurany/${sura.reciterName}/${SuraUtil.getSuraName(sura.id)}.mp3"
      if (!this.checkIfSuraExist(subPath)) {
        if (this.isNetworkOk()) {
          toastySuccess(R.string.alrt_download_start_title)
          val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
          val request = DownloadManager.Request(Uri.parse(sura.suraUrl))
          request.setAllowedNetworkTypes(
            DownloadManager.Request.NETWORK_WIFI or
                DownloadManager.Request.NETWORK_MOBILE
          )
            .setAllowedOverRoaming(false)
            .setTitle(SuraUtil.getSuraName(sura.id))
            .setDescription(SuraUtil.getSuraName(sura.id) + "| " + sura.reciterName)
            .setDestinationInExternalFilesDir(this, null, subPath)
          downloadId = downloadManager.enqueue(request)
        } else {
          toastyError(R.string.alrt_no_internet_msg)
          stopSelf()
        }
      } else {
        toastyInfo(R.string.alrt_sura_exist_message)
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
        toastySuccess(R.string.alrt_download_completed_msg)
        scan()
      } else {
        Timber.d(TAG, "onReceive: download id not match")
      }
    }
  }

  fun scan() {
    val file = File(this.getSuraPath(subPath))
    MediaScannerConnection.scanFile(
      this, arrayOf(file.toString()),
      arrayOf(file.name), null
    )
  }

  override fun onDestroy() {
    unregisterReceiver(onComplete)
    super.onDestroy()
  }

  companion object {
    private val TAG = QuranyDownloadService::class.java.name
  }
}