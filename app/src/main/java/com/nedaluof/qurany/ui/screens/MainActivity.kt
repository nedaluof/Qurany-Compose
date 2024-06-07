package com.nedaluof.qurany.ui.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.nedaluof.qurany.data.model.Reciter
import com.nedaluof.qurany.di.LocaleManagerEntryPoint
import com.nedaluof.qurany.ui.screens.splash.SplashScreen
import com.nedaluof.qurany.ui.screens.suras.SurasActivity
import com.nedaluof.qurany.ui.theme.QuranyComposeTheme
import com.nedaluof.qurany.util.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private val viewModel by viewModels<MainViewModel>()
  override fun attachBaseContext(newBase: Context?) {
    val localeManager = EntryPointAccessors.fromApplication(
      newBase!!,
      LocaleManagerEntryPoint::class.java
    ).localeManager
    super.attachBaseContext(newBase.let { localeManager.configureAppLocale(it) })
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val isSplashShown by remember { viewModel.splashScreenShown }
      LaunchedEffect(isSplashShown) {
        if (!isSplashShown) {
          delay(2200L)
          viewModel.splashScreenShown.value = true
        }
      }
      CompositionLocalProvider(
        androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current,
      ) {
        QuranyComposeTheme(viewModel.isNightModeEnabled.value) {
          if (isSplashShown) {
            MainScreen(viewModel = viewModel, reloadApp = {
              with(this@MainActivity) {
                startActivity(getIntent(this))
                finish()
              }
            }, onReciterClicked = ::openSurasActivity)
          } else {
            SplashScreen()
          }
        }
      }
    }
  }

  private fun openSurasActivity(reciter: Reciter) {
    startActivity(
      Intent(this@MainActivity, SurasActivity::class.java).putExtra(
        AppConstants.RECITER_KEY,
        reciter
      )
    )
  }

  companion object {
    fun getIntent(
      context: Context
    ) = Intent(context, MainActivity::class.java)
  }
}