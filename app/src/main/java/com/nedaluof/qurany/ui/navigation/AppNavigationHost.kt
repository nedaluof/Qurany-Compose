package com.nedaluof.qurany.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nedaluof.data.model.ReciterModel
import com.nedaluof.qurany.ui.screens.MainViewModel
import com.nedaluof.qurany.ui.screens.main.MainScreen
import com.nedaluof.qurany.ui.screens.splash.SplashScreen
import com.nedaluof.qurany.ui.screens.suras.SurasListScreen
import kotlinx.coroutines.delay

/**
 * Created By NedaluOf - 6/8/2024.
 */
@Composable
fun AppNavigationHost(
  modifier: Modifier = Modifier,
  navController: NavHostController,
  mainViewModel: MainViewModel
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
  var reciter by remember { mutableStateOf<ReciterModel?>(null) }
  NavHost(
    navController = navController,
    startDestination = AppNavigationScreens.Splash.route,
    modifier = modifier
  ) {
    composable(AppNavigationScreens.Splash.route) {
      SplashScreen()
    }
    composable(AppNavigationScreens.Main.route) {
      MainScreen(viewModel = mainViewModel) { comingReciter ->
        reciter = comingReciter
        navController.navigate(AppNavigationScreens.Suras.route)
      }
    }
    composable(route = AppNavigationScreens.Suras.route) {
      reciter?.let {
        SurasListScreen(reciter = reciter!!) {
          reciter = null
          navController.popBackStack()
        }
      }
    }
  }
}

sealed class AppNavigationScreens(val route: String) {
  data object Splash : AppNavigationScreens("splash")
  data object Main : AppNavigationScreens("main")
  data object Suras : AppNavigationScreens("suras")
}