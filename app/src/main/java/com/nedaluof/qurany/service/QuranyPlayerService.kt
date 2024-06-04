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
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.NotificationUtil
import com.nedaluof.qurany.R
import com.nedaluof.qurany.data.model.Reciter
import com.nedaluof.qurany.data.model.SuraModel
import com.nedaluof.qurany.ui.screens.main.suras.SurasActivity
import com.nedaluof.qurany.util.AppConstants
import com.nedaluof.qurany.util.getLogoAsBitmap
import com.nedaluof.qurany.util.getSuraPath
import com.nedaluof.qurany.util.isNetworkOk
import com.nedaluof.qurany.util.parcelable
import com.nedaluof.qurany.util.toastyError
import com.nedaluof.qurany.util.toastySuccess

/**
 * Created by nedaluof on 12/27/2020.
 */
class QuranyPlayerService : Service() {

  private val player: ExoPlayer by lazy {
    ExoPlayer.Builder(this).build()
  }

  private var playerNotificationManager: PlayerNotificationManager? = null
  lateinit var sura: SuraModel // coming sura to run on the player
  lateinit var reciter: Reciter // coming reciter to serve the state of the suras activity
  private var isRunning = false

  // binder of the service to connect to the PlayerView in [SurasActivity]
  private var playerBinder = PlayerBinder()
  override fun onBind(intent: Intent?): IBinder = playerBinder

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    sura = intent?.parcelable(AppConstants.SURA_KEY)!!
    reciter = intent.parcelable(AppConstants.RECITER_KEY)!!
    startPlayer()
    isRunning = true
    return START_NOT_STICKY
  }

  private fun startPlayer() {
    if (this::sura.isInitialized) {
      when (sura.playingType) {
        AppConstants.PLAYING_ONLINE -> {
          if (this.isNetworkOk()) {
            val suraURI = Uri.parse(sura.suraUrl)
            val suraMediaSource = buildMediaSource(suraURI)
            player.apply {
              setMediaSource(suraMediaSource)
              prepare()
              playWhenReady = true
            }
            this.toastySuccess(R.string.alrt_sura_playing_online)
          } else {
            toastyError(R.string.alrt_no_internet_msg)
          }
        }

        AppConstants.PLAYING_LOCALLY -> {
          val localSuraPath = this.getSuraPath(sura.suraSubPath)
          val suraURI = Uri.parse(localSuraPath)
          val suraMediaSource = buildMediaSource(suraURI)
          player.apply {
            setMediaSource(suraMediaSource)
            prepare()
            playWhenReady = true
          }
          this@QuranyPlayerService.toastySuccess(R.string.alrt_sura_playing_locally)
        }
      }
      initPlayerNotification()
    }
  }

  fun playerIsRunning() = isRunning

  fun getCurrentSuraRunning() = if (this::sura.isInitialized) {
    sura
  } else {
    SuraModel()
  }

  private fun initPlayerNotification() {
    val mediaDescriptionAdapter = object : PlayerNotificationManager.MediaDescriptionAdapter {
      override fun getCurrentContentTitle(player: Player): CharSequence =
        sura.reciterName

      override fun createCurrentContentIntent(player: Player): PendingIntent? {
        val resultIntent =
          Intent(this@QuranyPlayerService, SurasActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
            reciter.isPlayingNow = true
            putExtra(AppConstants.RECITER_KEY, reciter)
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
        player: Player,
        callback: PlayerNotificationManager.BitmapCallback
      ) =
        this@QuranyPlayerService.getLogoAsBitmap()
    }

    val notificationListener = object :
      PlayerNotificationManager.NotificationListener {
      override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        stopSelf()
      }

      override fun onNotificationPosted(
        notificationId: Int,
        notification: Notification,
        ongoing: Boolean
      ) {
        if (ongoing) {
          startForeground(notificationId, notification)
        } else {
          stopForegroundT(false)
        }
      }
    }
    playerNotificationManager =
      PlayerNotificationManager.Builder(
        this,
        1,
        R.string.notification_id.toString(),
        mediaDescriptionAdapter
      )
        //.setMediaDescriptionAdapter(mediaDescriptionAdapter)
        .setNotificationListener(notificationListener)
        .setChannelImportance(NotificationUtil.IMPORTANCE_HIGH)
        .setChannelNameResourceId(R.string.notification_channel_des)
        .build()
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
    //val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(this)
    val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(this)
    val mediaItem = MediaItem.Builder().setUri(uri).build()
    return ProgressiveMediaSource.Factory(dataSourceFactory)
      .createMediaSource(mediaItem)
  }

  private fun stopForegroundT(removeNotification: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      stopForeground(if (removeNotification) STOP_FOREGROUND_REMOVE else STOP_FOREGROUND_DETACH)
    } else {
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
}