package com.nedaluof.qurany.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import com.nedaluof.qurany.di.LocaleManagerEntryPoint
import com.nedaluof.qurany.ui.screens.main.MainScreen
import com.nedaluof.qurany.ui.screens.splash.SplashActivity
import com.nedaluof.qurany.ui.theme.QuranyComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

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
      CompositionLocalProvider(
        androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current,
      ) {
        QuranyComposeTheme() {
          MainScreen(reloadApp = {
            with(this@MainActivity) {
              startActivity(SplashActivity.getIntent(this))
              finish()
            }
          })
        }
      }
    }
  }

  companion object {
    fun getIntent(
      context: Context
    ) = Intent(context, MainActivity::class.java)
  }
}