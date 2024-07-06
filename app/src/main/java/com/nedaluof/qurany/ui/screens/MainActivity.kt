package com.nedaluof.qurany.ui.screens

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nedaluof.qurany.R
import com.nedaluof.qurany.ui.navigation.AppNavigationHost
import com.nedaluof.qurany.ui.theme.QuranyComposeTheme
import com.nedaluof.qurany.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  //region variables
  private val viewModel by viewModels<MainViewModel>()
  //endregion

  //region
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val navController = rememberNavController()
      val navBackStackEntry by navController.currentBackStackEntryAsState()
      val context = LocalContext.current
      val scope = rememberCoroutineScope()
      var isBackPressedCounter by remember { mutableIntStateOf(0) }
      CompositionLocalProvider(
        androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current,
      ) {
        val isNightMode by viewModel.isNightModeEnabled.collectAsStateWithLifecycle()
        QuranyComposeTheme(isNightMode) {
          AppNavigationHost(
            navController = navController,
            mainViewModel = viewModel
          )
          BackHandler {
            when (navBackStackEntry?.destination?.route) {
              "main" -> {
                isBackPressedCounter++
                context.toast(R.string.alrt_exit_app_msg)
                scope.launch {
                  delay(2000L)
                  isBackPressedCounter = 0
                }
                if (isBackPressedCounter >= 2) {
                  this@MainActivity.finish()
                }
              }

              else -> navController.popBackStack()
            }
          }
        }
      }
    }
  }
  //endregion
}