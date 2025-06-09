package com.nedaluof.qurany.ui.features

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.LocaleManager
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.core.animation.doOnEnd
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nedaluof.qurany.R
import com.nedaluof.qurany.ui.navigation.QuranyNavHost
import com.nedaluof.qurany.ui.theme.QuranyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  //region variables
  private val viewModel by viewModels<MainViewModel>()
  //endregion

  //region
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    //enableEdgeToEdge()
    setDefaultAppLocale()
    initSplashScreen()
    setContent {
      CompositionLocalProvider(
        LocalLifecycleOwner provides LocalLifecycleOwner.current,
      ) {
        val isNightMode by viewModel.isNightModeEnabledFlow.collectAsStateWithLifecycle()
        QuranyTheme(isNightMode) {
          QuranyNavHost()
        }
      }
    }
  }

  private fun setDefaultAppLocale() {
    if (AppCompatDelegate.getApplicationLocales().isEmpty) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSystemService(LocaleManager::class.java)
          .applicationLocales = LocaleList.forLanguageTags("ar")
      } else {
        AppCompatDelegate.setApplicationLocales(
          LocaleListCompat.forLanguageTags("ar")
        )
      }
    }
  }

  private fun initSplashScreen() {
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        installSplashScreen().apply {
          splashScreen.setSplashScreenTheme(
            if (viewModel.isNightMode) R.style.Theme_Qurany_Dark_Splash
            else R.style.Theme_Qurany_Light_Splash
          )
          setOnExitAnimationListener { screen ->
            ObjectAnimator.ofPropertyValuesHolder(
              screen.iconView,
              PropertyValuesHolder.ofFloat("scaleX", 1.2f),
              PropertyValuesHolder.ofFloat("scaleY", 1.2f)
            ).apply {
              duration = 250
              repeatCount = 1
              repeatMode = ObjectAnimator.REVERSE
              doOnEnd { screen.remove() }
            }.start()
          }
        }
      }
    } catch (_: Exception) {
    }
  }
  //endregion
}