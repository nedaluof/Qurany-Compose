package com.nedaluof.qurany.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nedaluof.data.model.ReciterModel
import com.nedaluof.qurany.R
import com.nedaluof.qurany.ui.screens.reciters.RecitersListScreen

/**
 * Created By NedaluOf - 6/16/2024.
 */


@Composable
fun BottomNavigationHost(
  navController: NavHostController, paddingValues: PaddingValues,
  onReciterClicked: (ReciterModel) -> Unit
) {
  NavHost(
    navController = navController,
    startDestination = BottomNavigationScreens.Reciters.route,
    modifier = Modifier.padding(paddingValues)
  ) {
    composable(BottomNavigationScreens.Reciters.route) {
      RecitersListScreen(Modifier.fillMaxSize(), onReciterClicked = onReciterClicked)
    }
    composable(BottomNavigationScreens.MyReciters.route) {
      RecitersListScreen(Modifier.fillMaxSize(), true, onReciterClicked = onReciterClicked)
    }
  }
}

sealed class BottomNavigationScreens(
  val route: String, @StringRes val resourceId: Int, @DrawableRes val icon: Int
) {
  data object Reciters :
    BottomNavigationScreens("reciters", R.string.reciters_nav_label, R.drawable.ic_home_navigation)

  data object MyReciters : BottomNavigationScreens(
    "favorites", R.string.favorites_nav_label, R.drawable.ic_favorite_navigation
  )
}