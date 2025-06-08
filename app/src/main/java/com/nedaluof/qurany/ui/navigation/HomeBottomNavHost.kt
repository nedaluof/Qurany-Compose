package com.nedaluof.qurany.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nedaluof.data.model.ReciterModel
import com.nedaluof.qurany.R
import com.nedaluof.qurany.ui.features.reciters.RecitersListScreen

/**
 * Created By NedaluOf - 6/16/2024.
 */
@Composable
fun HomeBottomNavHost(
  navController: NavHostController,
  onReciterClicked: (ReciterModel) -> Unit
) {
  NavHost(
    navController = navController,
    startDestination = HomeBottomNavScreens.Reciters.route,
  ) {
    composable(HomeBottomNavScreens.Reciters.route) {
      RecitersListScreen(onReciterClicked = onReciterClicked)
    }
    composable(HomeBottomNavScreens.MyReciters.route) {
      RecitersListScreen(isForFavorites = true, onReciterClicked = onReciterClicked)
    }
  }
}

sealed class HomeBottomNavScreens(
  val route: String,
  @StringRes val resourceId: Int,
  @DrawableRes val icon: Int
) {
  data object Reciters :
    HomeBottomNavScreens(
      "reciters_screen",
      R.string.reciters_nav_label,
      R.drawable.ic_home_navigation
    )

  data object MyReciters : HomeBottomNavScreens(
    "favorites_screen", R.string.favorites_nav_label, R.drawable.ic_favorite_navigation
  )
}