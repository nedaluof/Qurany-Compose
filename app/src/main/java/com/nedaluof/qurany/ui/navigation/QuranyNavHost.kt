package com.nedaluof.qurany.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nedaluof.qurany.R
import com.nedaluof.qurany.ui.features.home.HomeScreen
import com.nedaluof.qurany.ui.features.splash.SplashScreen
import com.nedaluof.qurany.ui.features.suras.SurasScreen
import com.nedaluof.qurany.util.getActivity
import com.nedaluof.qurany.util.toast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created By NedaluOf - 6/8/2024.
 */
@Composable
fun QuranyNavHost(
  modifier: Modifier = Modifier
) {
  val navController = rememberNavController()
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  var isBackPressedCounter by remember { mutableIntStateOf(0) }

  NavHost(
    modifier = modifier,
    navController = navController,
    startDestination = QuranyScreens.SPLASH.route
  ) {
    composable(QuranyScreens.SPLASH.route) {
      SplashScreen(navController = navController)
    }
    composable(QuranyScreens.HOME.route) {
      HomeScreen(
        onReciterClicked = { reciterId ->
          navController.navigate("${QuranyScreens.SURAS.route}/$reciterId")
        }
      )
    }
    composable(
      route = "${QuranyScreens.SURAS.route}/{reciter_id}",
      arguments = listOf(navArgument("reciter_id") {
        type = NavType.IntType
      })
    ) { backStackEntry ->
      backStackEntry.arguments?.getInt("reciter_id")?.let { reciterId ->
        SurasScreen(reciterId = reciterId, onBackPressed = navController::popBackStack)
      }
    }
  }

  BackHandler {
    when (navBackStackEntry?.destination?.route) {
      "main" -> {
        isBackPressedCounter++
        context.toast(R.string.alert_exit_app_message)
        scope.launch {
          delay(2000L)
          isBackPressedCounter = 0
        }
        if (isBackPressedCounter >= 2) {
          context.getActivity()?.finish()
        }
      }

      else -> navController.popBackStack()
    }
  }
}