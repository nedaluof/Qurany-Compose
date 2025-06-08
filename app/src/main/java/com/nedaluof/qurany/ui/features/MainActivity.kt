package com.nedaluof.qurany.ui.features

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
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
    setContent {
      QuranyApp()
    }
  }

  @Composable
  private fun QuranyApp() {
    CompositionLocalProvider(
      LocalLifecycleOwner provides LocalLifecycleOwner.current,
    ) {
      val isNightMode by viewModel.isNightModeEnabled.collectAsStateWithLifecycle()
      QuranyTheme(isNightMode) {
        QuranyNavHost()
      }
    }
  }
  //endregion
}