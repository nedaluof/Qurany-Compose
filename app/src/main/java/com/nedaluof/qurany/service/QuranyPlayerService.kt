package com.nedaluof.qurany.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.TaskStackBuilder
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.NotificationUtil
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerNotificationManager
import com.nedaluof.qurany.R
import com.nedaluof.qurany.data.model.SuraModel
import com.nedaluof.qurany.ui.screens.MainActivity
import com.nedaluof.qurany.util.getLogoAsBitmap
import com.nedaluof.qurany.util.getSuraPath
import com.nedaluof.qurany.util.isNetworkOk
import com.nedaluof.qurany.util.parcelable
import com.nedaluof.qurany.util.toast

/**
 * Created by nedaluof on 12/27/2020.
 */
class QuranyPlayerService : Service() {

  private val player: ExoPlayer by lazy {
    ExoPlayer.Builder(this).build()
  }

  private var playerNotificationManager: PlayerNotificationManager? = null
  lateinit var sura: SuraModel // coming sura to run on the player
  private var isRunning = false

  // binder of the service to connect to the PlayerView in [SurasActivity]
  private var playerBinder = PlayerBinder()
  override fun onBind(intent: Intent?): IBinder = playerBinder

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    sura = intent?.parcelable(SURA_KEY)!!
    startPlayer()
    isRunning = true
    return START_NOT_STICKY
  }

  private fun startPlayer() {
    if (this::sura.isInitialized) {
      if (sura.isSuraExistInLocalStorage) {
        val localSuraPath = this.getSuraPath(sura.suraSubPath)
        val suraURI = Uri.parse(localSuraPath)
        val suraMediaSource = buildMediaSource(suraURI)
        player.apply {
          setMediaSource(suraMediaSource)
          prepare()
          playWhenReady = true
        }
        this@QuranyPlayerService.toast(R.string.alrt_sura_playing_locally)
      } else {
        if (this.isNetworkOk()) {
          val suraURI = Uri.parse(sura.suraUrl)
          val suraMediaSource = buildMediaSource(suraURI)
          player.apply {
            setMediaSource(suraMediaSource)
            prepare()
            playWhenReady = true
          }
          this.toast(R.string.alrt_sura_playing_online)
        } else {
          toast(R.string.alrt_no_internet_msg)
        }
      }
      initPlayerNotification()
    }
  }

  private fun initPlayerNotification() {
    val mediaDescriptionAdapter = object : PlayerNotificationManager.MediaDescriptionAdapter {
      override fun getCurrentContentTitle(player: Player): CharSequence = sura.reciterName

      override fun createCurrentContentIntent(player: Player): PendingIntent? {
        val resultIntent = Intent(this@QuranyPlayerService, MainActivity::class.java).also {
          it.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
        }
        // Create the TaskStackBuilder
        return TaskStackBuilder.create(this@QuranyPlayerService).run {
          addNextIntentWithParentStack(resultIntent)
          getPendingIntent(
            System.currentTimeMillis().toInt(),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE else PendingIntent.FLAG_UPDATE_CURRENT
          )
        }
      }

      override fun getCurrentContentText(player: Player): CharSequence = sura.name
      override fun getCurrentLargeIcon(
        player: Player, callback: PlayerNotificationManager.BitmapCallback
      ) = this@QuranyPlayerService.getLogoAsBitmap()
    }

    val notificationListener = object : PlayerNotificationManager.NotificationListener {
      override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        stopSelf()
      }

      override fun onNotificationPosted(
        notificationId: Int, notification: Notification, ongoing: Boolean
      ) {
        if (ongoing) {
          startForeground(notificationId, notification)
        } else {
          stopForegroundT(false)
        }
      }
    }
    playerNotificationManager = PlayerNotificationManager.Builder(
      this, 1, R.string.notification_id.toString()
    )
      .setMediaDescriptionAdapter(mediaDescriptionAdapter)
      .setNotificationListener(notificationListener)
      .setChannelImportance(NotificationUtil.IMPORTANCE_HIGH)
      .setChannelNameResourceId(R.string.notification_channel_des).build()
      .also { it.setPlayer(player) }
  }

  fun getPlayerInstance(): ExoPlayer {
    if (!player.isPlaying) {
      startPlayer()
    }
    return player
  }

  private fun releasePlayer() {
    playerNotificationManager?.setPlayer(null)
    player.release()
  }

  private fun buildMediaSource(uri: Uri): MediaSource {
    val dataSourceFactory = DefaultDataSource.Factory(this)
    val mediaItem = MediaItem.Builder().setUri(uri).build()
    return ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
  }

  private fun stopForegroundT(removeNotification: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      stopForeground(if (removeNotification) STOP_FOREGROUND_REMOVE else STOP_FOREGROUND_DETACH)
    } else {
      @Suppress("DEPRECATION")
      stopForeground(removeNotification)
    }
  }

  fun stop() {
    stopForegroundT(true)
    stopSelf()
  }

  override fun onDestroy() {
    releasePlayer()
    super.onDestroy()
  }

  inner class PlayerBinder : Binder() {
    val playerService: QuranyPlayerService = this@QuranyPlayerService
  }

  companion object {
    const val SURA_KEY = "SURA_KEY"
  }
}