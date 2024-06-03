package com.nedaluof.qurany.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.nedaluof.qurany.R

/**
 * Created By NedaluOf - 5/31/2024.
 */
sealed class BottomNavigationScreens(
  val route: String, @StringRes val resourceId: Int, @DrawableRes val icon: Int
) {
  data object Reciters :
    BottomNavigationScreens("reciters", R.string.home_nav_title, R.drawable.ic_home_navigation)

  data object MyReciters : BottomNavigationScreens(
    "my_reciters", R.string.favorite_nav_title, R.drawable.ic_favorite_navigation
  )
}