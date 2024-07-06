@file:UnstableApi

package com.nedaluof.qurany.service

import android.content.Intent
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.nedaluof.data.util.catchOn

/**
 * Created by nedaluof on 12/27/2020.
 * Updated by nedaluof on 6/13/2024.
 */
class QuranyPlayerService : MediaSessionService() {

  //region variables
  private var mediaSession: MediaSession? = null
  //endregion

  //region logic
  override fun onCreate() {
    super.onCreate()
    catchOn({
      mediaSession = MediaSession.Builder(
        this, ExoPlayer.Builder(this).build()
      ).setShowPlayButtonIfPlaybackIsSuppressed(false).build()
    })
  }

  override fun onTaskRemoved(rootIntent: Intent?) {
    val player = mediaSession?.player
    player?.let {
      if (!player.playWhenReady || player.mediaItemCount == 0 || player.playbackState == Player.STATE_ENDED) {
        stopSelf()
      }
    }
  }

  override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaSession

  override fun onDestroy() {
    mediaSession?.run {
      player.release()
      release()
      mediaSession = null
    }
    super.onDestroy()
  }
  //endregion
}