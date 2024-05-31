package com.nedaluof.qurany.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.nedaluof.qurany.BR
import com.nedaluof.qurany.R
import com.nedaluof.qurany.databinding.ActivityMainBinding
import com.nedaluof.qurany.new_ui.screens.splash.SplashActivity
import com.nedaluof.qurany.ui.base.BaseActivity
import com.nedaluof.qurany.ui.main.myreciters.MyRecitersFragment
import com.nedaluof.qurany.ui.main.reciters.RecitersFragment
import com.nedaluof.qurany.util.click
import com.nedaluof.qurany.util.toast
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by nedaluof on 6/2020. {Java}
 * Created by nedaluof on 12/13/2020. {Kotlin}
 * Updated by nedaluof on 9/11/2021.
 */
@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

  override val layoutId = R.layout.activity_main
  override val bindingVariable = BR.viewmodel
  private val mainViewModel by viewModels<MainViewModel>()
  override fun getViewModel() = mainViewModel

  private var doubleBackToExitPressedOnce = true

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mainViewModel.loadAppLanguage()
    observeViewModel()
    initNavigation()
    initClicks()
    loadFragment(RecitersFragment())
  }

  private fun initNavigation() {
    with(binding.navigation) {
      setItemEnabled(R.id.nav_reciters, true)
      setItemSelected(R.id.nav_reciters, true)
      setOnItemSelectedListener { i ->
        when (i) {
          R.id.nav_reciters -> {
            binding.navigation.setItemSelected(R.id.nav_reciters, true)
            loadFragment(RecitersFragment())
          }

          R.id.nav_my_reciters -> {
            binding.navigation.setItemSelected(R.id.nav_my_reciters, true)
            loadFragment(MyRecitersFragment())
          }
        }
      }
    }
  }

  private fun loadFragment(fragment: Fragment) {
    supportFragmentManager
      .beginTransaction()
      .replace(binding.container.id, fragment)
      .commit()
  }

  private fun initClicks() {
    with(binding) {
      appLightNightButton.click {
        mainViewModel.changeDayNightMode()
      }

      languageTextView.click {
        mainViewModel.changeAppLanguage()
        startActivity(SplashActivity.getIntent(this@MainActivity))
        finish()
      }
    }
  }

  private fun observeViewModel() {
    mainViewModel.isNightModeEnabled.collectFlow { enabled ->
      if (enabled != null) {
        val mode = if (enabled) {
          AppCompatDelegate.MODE_NIGHT_YES
        } else {
          AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
        }
        binding.appLightNightButton.setImageDrawable(
          ContextCompat.getDrawable(
            this, if (enabled) R.drawable.ic_light_mode else R.drawable.ic_night_mode
          )
        )
        AppCompatDelegate.setDefaultNightMode(mode)
      }
    }
  }

  override fun onBackPressed() {
    val fragment = supportFragmentManager.findFragmentById(binding.container.id)
    if (fragment is RecitersFragment) {
      if (doubleBackToExitPressedOnce) {
        doubleBackToExitPressedOnce = false
        toast(R.string.exit_app_msg)
        Handler(Looper.myLooper()!!).postDelayed(
          {
            doubleBackToExitPressedOnce = true
          },
          2000
        )
      } else {
        super.onBackPressed()
      }
    } else if (fragment is MyRecitersFragment) {
      loadFragment(RecitersFragment())
      binding.navigation.setItemSelected(R.id.nav_reciters, true)
    }
  }


  companion object {
    fun getIntent(
      context: Context
    ) = Intent(context, MainActivity::class.java)
  }
}