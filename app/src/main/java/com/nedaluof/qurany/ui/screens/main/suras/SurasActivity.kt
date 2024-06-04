package com.nedaluof.qurany.ui.screens.main.suras

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.util.Util
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.nedaluof.qurany.data.model.Reciter
import com.nedaluof.qurany.data.model.SuraModel
import com.nedaluof.qurany.service.QuranyDownloadService
import com.nedaluof.qurany.service.QuranyPlayerService
import com.nedaluof.qurany.ui.theme.QuranyComposeTheme
import com.nedaluof.qurany.util.AppConstants
import com.nedaluof.qurany.util.parcelable
import com.nedaluof.qurany.util.postDelayed
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Created by nedaluof on 12/5/2020.
 * Updated by nedaluof on 9/13/2021.
 * Updated by NedaluOf on 6/4/2024.
 */
@AndroidEntryPoint
class SurasActivity : AppCompatActivity() {

  //region variables
  private val surasViewModel: SurasViewModel by viewModels()

  private lateinit var reciterData: Reciter
  private var sheetBehavior: BottomSheetBehavior<*>? = null

  // Player & QuranyPlayerService
  private var exoPlayer: ExoPlayer? = null
  private var service: QuranyPlayerService? = null

  private val quranyPlayerServiceIntent: Intent by lazy {
    Intent(this, QuranyPlayerService::class.java)
  }
  private var serviceConnection: ServiceConnection? = null
  private var bound = false
  //endregion

  @OptIn(ExperimentalMaterial3Api::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    reciterData = intent?.parcelable(AppConstants.RECITER_KEY)!!
    initServiceConnection()
    setContent {
      QuranyComposeTheme {
        val sheetState = rememberModalBottomSheetState()
        var showPlayerSheet by remember { surasViewModel.playerSheetVisibility }
        Box(modifier = Modifier.fillMaxSize()) {
          SurasListScreen(
            reciter = reciterData,
            onPlayClicked = ::onPlaySuraRequested,
            onDownloadClicked = ::onDownloadSuraRequested
          )

          if (showPlayerSheet) {
            QuranyPlayerSheet(exoPlayer = exoPlayer,
              sheetState = sheetState,
              onClosePlayerClicked = {
                stopService()
                showPlayerSheet = false
              }) {}
          }
        }
      }
    }
  }

  private fun initServiceConnection() {
    serviceConnection = object : ServiceConnection {
      override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
        val binder = iBinder as QuranyPlayerService.PlayerBinder
        service = binder.playerService
        bound = true
        initializePlayer()
      }

      override fun onServiceDisconnected(componentName: ComponentName) {
        bound = false
      }
    }
    bindService(quranyPlayerServiceIntent, serviceConnection!!, Context.BIND_AUTO_CREATE)
  }

  private fun initializePlayer() {
    if (bound) {
      exoPlayer = service?.getPlayerInstance()!!
      if (service?.playerIsRunning()!!) {
        {
          surasViewModel.playerSheetVisibility.value = true
        }.postDelayed(700)
      }
      initPlayerListener()
    }
  }

  private fun initRecyclerView() {/*with(binding.surasRecyclerView) {
      setHasFixedSize(true)
      addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
          super.onScrolled(recyclerView, dx, dy)
          if (dy > 0) {
            sheetBehavior?.setState(BottomSheetBehavior.STATE_COLLAPSED)
          } else if (dy < 0) {
            sheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
          }
        }
      })
    }*/
  }

  private fun onPlaySuraRequested(sura: SuraModel) {
    lifecycleScope.launch {
      surasViewModel.checkSuraExist(sura).collect { exist ->
        if (exist != null) {
          if (exist) sura.playingType = AppConstants.PLAYING_LOCALLY
          playSura(sura)
        }
      }
    }
  }

  private fun onDownloadSuraRequested(sura: SuraModel) {
    startService(
      Intent(this, QuranyDownloadService::class.java).putExtra(AppConstants.DOWNLOAD_SURA_KEY, sura)
    )
  }

  private fun playSura(sura: SuraModel) {
    quranyPlayerServiceIntent.apply {
      putExtra(AppConstants.SURA_KEY, sura)
      putExtra(AppConstants.RECITER_KEY, reciterData)
    }
    exoPlayer?.let { exoPlayer ->
      if (!exoPlayer.isPlaying) {
        bindService(
          quranyPlayerServiceIntent, serviceConnection!!, Context.BIND_AUTO_CREATE
        )
      }
    } ?: run {
      service?.stopSelf()
      unbindService(serviceConnection!!)
      bound = false
      bindService(
        quranyPlayerServiceIntent, serviceConnection!!, Context.BIND_ADJUST_WITH_ACTIVITY
      )
    }
    reInitToPlaySura(sura)
    Util.startForegroundService(this@SurasActivity, quranyPlayerServiceIntent)
  }

  private fun reInitToPlaySura(sura: SuraModel) {/*with(binding.playerBottomSheet) {
      bottomSheet.isVisible = true
      reciterSuraName.text = sura.playerTitle
      sheetBehavior?.state = if (sura.playingType == AppConstants.PLAYING_ONLINE) {
        if (this@SurasActivity.isNetworkOk()) {
          BottomSheetBehavior.STATE_EXPANDED
        } else {
          BottomSheetBehavior.STATE_COLLAPSED
        }
      } else {
        BottomSheetBehavior.STATE_EXPANDED
      }
    }*/
  }

  private fun initPlayerListener() {
    exoPlayer?.addListener(object : Player.Listener {
      override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        if (playbackState == Player.STATE_ENDED) {
          { surasViewModel.playerSheetVisibility.value = false }.postDelayed(1500)
        }
      }
    })
  }

  private fun stopService() {
    if (bound) {
      service?.stop()
      unbindService(serviceConnection!!)
      bound = false
      //binding.playerBottomSheet.playerController.player = null
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    // Todo: if user close the Activity need efficient solution
    stopService()
  }
}