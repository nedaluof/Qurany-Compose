package com.nedaluof.qurany.ui.main.suras

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.util.Util
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.nedaluof.qurany.BR
import com.nedaluof.qurany.R
import com.nedaluof.qurany.data.model.Reciter
import com.nedaluof.qurany.data.model.SuraModel
import com.nedaluof.qurany.databinding.ActivitySurasBinding
import com.nedaluof.qurany.service.QuranyDownloadService
import com.nedaluof.qurany.service.QuranyPlayerService
import com.nedaluof.qurany.ui.base.BaseActivity
import com.nedaluof.qurany.util.AppConstants
import com.nedaluof.qurany.util.click
import com.nedaluof.qurany.util.getParcelableExtraT
import com.nedaluof.qurany.util.isNetworkOk
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Created by nedaluof on 12/5/2020.
 * Updated by nedaluof on 9/13/2021.
 */
@AndroidEntryPoint
class SurasActivity : BaseActivity<ActivitySurasBinding>() {

  override val layoutId = R.layout.activity_suras
  override val bindingVariable = BR.viewmodel
  private val surasViewModel: SurasViewModel by viewModels()
  override fun getViewModel() = surasViewModel

  private lateinit var reciterData: Reciter
  private var sheetBehavior: BottomSheetBehavior<*>? = null

  // Player & QuranyPlayerService
  private lateinit var exoPlayer: ExoPlayer
  private var service: QuranyPlayerService? = null

  private val quranyPlayerServiceIntent: Intent by lazy {
    Intent(this, QuranyPlayerService::class.java)
  }
  private var serviceConnection: ServiceConnection? = null
  private var bound = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding.backButton.click(::finish)
    loadComingIntent()
  }

  private fun loadComingIntent() {
    reciterData = intent?.getParcelableExtraT(AppConstants.RECITER_KEY)!!
    surasViewModel.loadSurasToUI(reciterData)
    initComponents()
  }

  private fun initComponents() {
    initBottomSheetBehavior()
    initRecyclerView()
    initServiceConnection()
  }

  private fun initBottomSheetBehavior() {
    with(binding.playerBottomSheet) {
      sheetBehavior = BottomSheetBehavior.from<View>(bottomSheet)
      closeBtn.setOnClickListener {
        resetPlayerView()
        stopService()
      }
    }
  }

  private fun resetPlayerView() {
    with(binding.playerBottomSheet) {
      bottomSheet.isVisible = true
      reciterSuraName.text = getString(R.string.app_player)
      sheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
    }
    Handler(Looper.getMainLooper()).postDelayed(
      {
        binding.playerBottomSheet.bottomSheet.isVisible = true
      }, 750
    )
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
      binding.playerBottomSheet.playerController.player = exoPlayer
      if (service?.playerIsRunning()!!) {
        Handler(Looper.getMainLooper()).postDelayed(
          {
            with(binding.playerBottomSheet) {
              bottomSheet.isVisible = true
              sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
              reciterSuraName.text = service?.getCurrentSuraRunning()?.playerTitle
            }
          },
          700
        )
      }
      initPlayerListener()
    }
  }

  private fun initRecyclerView() {
    with(binding.surasRecyclerView) {
      adapter = SurasAdapter(
        { sura -> onClickPlay(sura) },
        { sura -> downloadSura(sura) }
      )
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
    }
  }

  private fun onClickPlay(sura: SuraModel) {
    with(surasViewModel) {
      lifecycleScope.launch {
        checkSuraExist(sura).collect { exist ->
          if (exist != null) {
            if (exist) sura.playingType = AppConstants.PLAYING_LOCALLY
            playSura(sura)
          }
        }
      }
    }
  }

  private fun playSura(sura: SuraModel) {
    quranyPlayerServiceIntent.apply {
      putExtra(AppConstants.SURA_KEY, sura)
      putExtra(AppConstants.RECITER_KEY, reciterData)
    }
    if (!this::exoPlayer.isInitialized || !exoPlayer.isPlaying) {
      bindService(
        quranyPlayerServiceIntent,
        serviceConnection!!,
        Context.BIND_AUTO_CREATE
      )
    } else {
      service?.stopSelf()
      unbindService(serviceConnection!!)
      bound = false
      bindService(
        quranyPlayerServiceIntent,
        serviceConnection!!,
        Context.BIND_ADJUST_WITH_ACTIVITY
      )
    }
    reInitToPlaySura(sura)
    // this startForegroundService if SDK >= 26 or startService(myIntent) for SDK < 26
    Util.startForegroundService(this@SurasActivity, quranyPlayerServiceIntent)
  }

  private fun reInitToPlaySura(sura: SuraModel) {
    with(binding.playerBottomSheet) {
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
    }
  }

  private fun downloadSura(sura: SuraModel) {
    startService(
      Intent(this, QuranyDownloadService::class.java)
        .putExtra(AppConstants.DOWNLOAD_SURA_KEY, sura)
    )
  }

  private fun initPlayerListener() {
    exoPlayer.addListener(object : Player.Listener {
      override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        if (playbackState == Player.STATE_ENDED) {
          sheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
          Handler(Looper.getMainLooper()).postDelayed(
            {
              binding.playerBottomSheet.bottomSheet.isVisible = false
            },
            1500
          )
          // stopService()
        }
      }
    })
  }

  private fun stopService() {
    if (bound) {
      service?.stop()
      unbindService(serviceConnection!!)
      bound = false
      binding.playerBottomSheet.playerController.player = null
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    // Todo: if user close the Activity need efficient solution
    stopService()
  }
}