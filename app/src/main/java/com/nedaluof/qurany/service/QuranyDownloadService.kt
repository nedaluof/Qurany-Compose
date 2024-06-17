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
import com.nedaluof.data.repositories.suras.SurasRepository
import com.nedaluof.data.util.catchOn
import com.nedaluof.qurany.R
import com.nedaluof.qurany.util.isInternetAvailable
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

  //region variables
  private var downloadId: Long = 0
  private lateinit var sura: SuraModel

  @Inject
  lateinit var surasRepository: SurasRepository
  //endregion

  //region logic
  override fun onBind(intent: Intent?): IBinder? = null

  @SuppressLint("UnspecifiedRegisterReceiverFlag")
  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    catchOn({
      sura = intent?.parcelable(DOWNLOAD_SURA_KEY)!!
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        registerReceiver(
          onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), RECEIVER_EXPORTED
        )
      } else {
        registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
      }
      startDownload()
    })
    return START_NOT_STICKY
  }

  private fun startDownload() {
    if (this::sura.isInitialized) {
      val isSuraFileExist = surasRepository.checkIfSuraExist(sura.suraSubPath)
      if (!isSuraFileExist) {
        if (this.isInternetAvailable()) {
          toast(R.string.alrt_download_start_title)
          val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
          val request = DownloadManager.Request(Uri.parse(sura.suraUrl))
          request.setAllowedNetworkTypes(
            DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE
          ).setAllowedOverRoaming(false)
            .setTitle(sura.name)
            .setDescription(sura.playerTitle)
            .setDestinationInExternalFilesDir(this, null, sura.suraSubPath)
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
      if (downloadId == id) {
        toast(R.string.alrt_download_completed_msg)
        runMediaScanner()
      }
    }
  }

  private fun runMediaScanner() {
    catchOn({
      val file = File(sura.suraSubPath)
      MediaScannerConnection.scanFile(
        this, arrayOf(file.toString()), arrayOf(file.name), null
      )
    })
  }

  override fun onDestroy() {
    unregisterReceiver(onComplete)
    super.onDestroy()
  }
  //endregion

  companion object {
    private const val DOWNLOAD_SURA_KEY = "DOWNLOAD_SURA_KEY"

    fun getIntent(
      context: Context, suraModel: SuraModel
    ) = Intent(context, QuranyDownloadService::class.java).also {
      it.putExtra(DOWNLOAD_SURA_KEY, suraModel)
    }
  }
}