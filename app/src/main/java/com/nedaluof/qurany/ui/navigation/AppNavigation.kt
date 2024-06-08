package com.nedaluof.qurany.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.android.exoplayer2.ExoPlayer
import com.nedaluof.qurany.data.model.Reciter
import com.nedaluof.qurany.data.model.SuraModel
import com.nedaluof.qurany.ui.screens.MainActivity
import com.nedaluof.qurany.ui.screens.MainScreen
import com.nedaluof.qurany.ui.screens.splash.SplashScreen
import com.nedaluof.qurany.ui.screens.suras.SurasListScreen
import kotlinx.coroutines.delay

/**
 * Created By NedaluOf - 6/8/2024.
 */
@Composable
fun AppNavigation(
  modifier: Modifier = Modifier,
  navController: NavHostController,
  getExoPlayer: () -> ExoPlayer?,
  onPlayClicked: (SuraModel) -> Unit,
  onDownloadClicked: (SuraModel) -> Unit,
  onStopPlaying: () -> Unit
) {
  var isSplashShown by remember { mutableStateOf(false) }
  LaunchedEffect(isSplashShown) {
    if (!isSplashShown) {
      delay(2200L)
      isSplashShown = true
    }
  }
  if (isSplashShown) {
    navController.navigate(route = AppNavigationScreens.Main.route)
  }
  val context = LocalContext.current
  var reciter by remember { mutableStateOf<Reciter?>(null) }
  NavHost(
    navController = navController,
    startDestination = AppNavigationScreens.Splash.route,
    modifier = modifier
  ) {
    composable(AppNavigationScreens.Splash.route) {
      SplashScreen()
    }
    composable(AppNavigationScreens.Main.route) {
      MainScreen(reloadApp = {
        with(context) {
          startActivity(MainActivity.getIntent(this))
          //context.finish()
        }
      }) { comingReciter ->
        reciter = comingReciter
        navController.navigate(AppNavigationScreens.Suras.route)
      }
    }
    composable(route = AppNavigationScreens.Suras.route) {
      reciter?.let {
        SurasListScreen(reciter = it,
          exoPlayer = getExoPlayer(),
          onPlayClicked = onPlayClicked,
          onDownloadClicked = onDownloadClicked,
          stopPlaying = onStopPlaying,
          onBackPressed = {
            navController.popBackStack()
            reciter = null
          })
      }
    }
  }
}