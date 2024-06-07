package com.nedaluof.qurany.ui.screens.suras

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.util.Util
import com.nedaluof.qurany.data.model.Reciter
import com.nedaluof.qurany.data.model.SuraModel
import com.nedaluof.qurany.databinding.PlayerBottomSheetLayoutBinding
import com.nedaluof.qurany.service.QuranyDownloadService
import com.nedaluof.qurany.service.QuranyPlayerService
import com.nedaluof.qurany.ui.theme.QuranyComposeTheme
import com.nedaluof.qurany.util.AppConstants
import com.nedaluof.qurany.util.isNetworkOk
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
        val coroutineScope = rememberCoroutineScope()
        val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
          bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Expanded
          )
        )
        val suraToPlay by remember { surasViewModel.currentPlayingSura }
        BottomSheetScaffold(sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
          sheetPeekHeight = if (suraToPlay == null) 0.dp else 60.dp,
          sheetDragHandle = null,
          scaffoldState = bottomSheetScaffoldState,
          sheetContent = {
            if (suraToPlay != null) {
              AndroidViewBinding(PlayerBottomSheetLayoutBinding::inflate) {
                playerController.player = exoPlayer
                closeBtn.setOnClickListener {
                  stopService()
                }
                reciterSuraName.text = suraToPlay?.playerTitle ?: ""
                coroutineScope.launch {
                  if (suraToPlay?.playingType == AppConstants.PLAYING_ONLINE) {
                    if (this@SurasActivity.isNetworkOk()) {
                      bottomSheetScaffoldState.bottomSheetState.expand()
                    } else {
                      bottomSheetScaffoldState.bottomSheetState.partialExpand()
                    }
                  } else {
                    bottomSheetScaffoldState.bottomSheetState.expand()
                  }
                }
              }
            }
          }) {
          SurasListScreen(reciter = reciterData, onPlayClicked = { sura ->
            coroutineScope.launch {
              bottomSheetScaffoldState.bottomSheetState.expand()
            }
            onPlaySuraRequested(sura)
          }, onDownloadClicked = ::onDownloadSuraRequested, onCloseClicked = {
            this.onBackPressedDispatcher.onBackPressed()
          }, onScrolled = {
            coroutineScope.launch {
              bottomSheetScaffoldState.bottomSheetState.partialExpand()
            }
          })
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
          surasViewModel.currentPlayingSura.value = null
        }.postDelayed(700)
      }
      exoPlayer?.addListener(object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
          super.onPlaybackStateChanged(playbackState)
          if (playbackState == Player.STATE_ENDED) {
            { surasViewModel.currentPlayingSura.value = null }.postDelayed(1500)
          }
        }
      })
    }
  }

  private fun onPlaySuraRequested(sura: SuraModel) {
    lifecycleScope.launch {
      surasViewModel.checkSuraExist(sura).collect { exist ->
        if (exist != null) {
          if (exist) sura.playingType = AppConstants.PLAYING_LOCALLY
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
          surasViewModel.currentPlayingSura.value = sura
          Util.startForegroundService(this@SurasActivity, quranyPlayerServiceIntent)
        }
      }
    }
  }

  private fun onDownloadSuraRequested(sura: SuraModel) {
    startService(
      Intent(this, QuranyDownloadService::class.java).putExtra(AppConstants.DOWNLOAD_SURA_KEY, sura)
    )
  }

  private fun stopService() {
    if (bound) {
      service?.stop()
      unbindService(serviceConnection!!)
      bound = false
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    // Todo: if user close the Activity need efficient solution
    stopService()
  }
}