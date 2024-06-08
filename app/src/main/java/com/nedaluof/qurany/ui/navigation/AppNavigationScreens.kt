package com.nedaluof.qurany.ui.navigation

/**
 * Created By NedaluOf - 6/8/2024.
 */

sealed class AppNavigationScreens(val route: String) {
  data object Splash : AppNavigationScreens("splash")
  data object Main : AppNavigationScreens("main")
  data object Suras : AppNavigationScreens("suras")
}