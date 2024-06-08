package com.nedaluof.qurany.ui.screens

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.util.Util
import com.nedaluof.qurany.data.model.SuraModel
import com.nedaluof.qurany.service.QuranyDownloadService
import com.nedaluof.qurany.service.QuranyPlayerService
import com.nedaluof.qurany.ui.navigation.AppNavigation
import com.nedaluof.qurany.ui.theme.QuranyComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  //region variables
  private val viewModel by viewModels<MainViewModel>()

  // Player & QuranyPlayerService
  private var exoPlayer: ExoPlayer? = null
  private var service: QuranyPlayerService? = null

  private val quranyPlayerServiceIntent: Intent by lazy {
    Intent(this, QuranyPlayerService::class.java)
  }
  private var serviceConnection: ServiceConnection? = null
  private var isServiceBound = false
  //endregion

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initServiceConnection()
    setContent {
      val navController = rememberNavController()
      val navBackStackEntry by navController.currentBackStackEntryAsState()
      CompositionLocalProvider(
        androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current,
      ) {
        QuranyComposeTheme(viewModel.isNightModeEnabled.value) {
          AppNavigation(
            navController = navController,
            mainViewModel = viewModel,
            getExoPlayer = { exoPlayer },
            onPlayClicked = ::onPlaySuraRequested,
            onDownloadClicked = ::onDownloadSuraRequested,
            onStopPlaying = ::stopService
          )
          BackHandler {
            if (navBackStackEntry?.destination?.route == "main") {
              this@MainActivity.finish()
            } else {
              navController.popBackStack()
            }
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
        isServiceBound = true
        initializePlayer()
      }

      override fun onServiceDisconnected(componentName: ComponentName) {
        isServiceBound = false
      }
    }
    bindService(quranyPlayerServiceIntent, serviceConnection!!, Context.BIND_AUTO_CREATE)
  }

  private fun initializePlayer() {
    if (isServiceBound) {
      exoPlayer = service?.getPlayerInstance()!!
    }
  }

  private fun onPlaySuraRequested(sura: SuraModel) {
    quranyPlayerServiceIntent.also {
      it.putExtra(QuranyPlayerService.SURA_KEY, sura)
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
      isServiceBound = false
      bindService(
        quranyPlayerServiceIntent, serviceConnection!!, Context.BIND_ADJUST_WITH_ACTIVITY
      )
    }
    Util.startForegroundService(this@MainActivity, quranyPlayerServiceIntent)
  }

  private fun onDownloadSuraRequested(sura: SuraModel) {
    startService(QuranyDownloadService.getIntent(this, sura))
  }

  private fun stopService() {
    if (isServiceBound) {
      service?.stop()
      unbindService(serviceConnection!!)
      isServiceBound = false
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    // Todo: if user close the Activity need efficient solution
    stopService()
  }
}