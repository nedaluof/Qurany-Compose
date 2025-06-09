package com.nedaluof.qurany.ui.features

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
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    setContent {
      CompositionLocalProvider(
        LocalLifecycleOwner provides LocalLifecycleOwner.current,
      ) {
        val isNightMode by viewModel.isNightModeEnabled.collectAsStateWithLifecycle()
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
  //endregion
}