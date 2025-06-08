package com.nedaluof.qurany.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nedaluof.data.model.ReciterModel
import com.nedaluof.qurany.R
import com.nedaluof.qurany.ui.features.home.HomeScreen
import com.nedaluof.qurany.ui.features.splash.SplashScreen
import com.nedaluof.qurany.ui.features.suras.SurasListScreen
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
  var reciter by remember { mutableStateOf<ReciterModel?>(null) }

  NavHost(
    modifier = modifier,
    navController = navController,
    startDestination = QuranyScreens.SPLASH.route
  ) {
    composable(QuranyScreens.SPLASH.route) {
      SplashScreen(navController = navController)
    }
    composable(QuranyScreens.HOME.route) {
      HomeScreen { comingReciter ->
        reciter = comingReciter
        navController.navigate(QuranyScreens.SURAS.route)
      }
    }
    composable(route = QuranyScreens.SURAS.route) {
      reciter?.let {
        SurasListScreen(reciter = reciter!!) {
          reciter = null
          navController.popBackStack()
        }
      }
    }
  }

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
          context.getActivity()?.finish()
        }
      }

      else -> navController.popBackStack()
    }
  }
}