package com.nedaluof.qurany.new_ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nedaluof.qurany.new_ui.navigation.BottomNavigationScreens
import com.nedaluof.qurany.new_ui.screens.main.myreciters.MyRecitersScreen
import com.nedaluof.qurany.new_ui.screens.main.reciters.RecitersScreen

/**
 * Created By NedaluOf - 5/31/2024.
 */

@Composable
fun MainScreen() {
  val navController = rememberNavController()
  val bottomNavigationItems = listOf(
    BottomNavigationScreens.Reciters, BottomNavigationScreens.MyReciters
  )
  Scaffold(bottomBar = {
    BottomNavigationBar(navController = navController, items = bottomNavigationItems)
  }) { paddingValues ->
    MainScreenNavigationConfigurations(navController = navController, paddingValues = paddingValues)
  }
}

@Composable
private fun BottomNavigationBar(
  navController: NavHostController, items: List<BottomNavigationScreens>
) {
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentDestination = navBackStackEntry?.destination?.route ?: "reciters"
  NavigationBar(
    modifier = Modifier
      .clip(RoundedCornerShape(topEnd = 50.dp, topStart = 50.dp))
      .border(
        width = 1.5.dp,
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(topEnd = 50.dp, topStart = 50.dp)
      )
      .background(MaterialTheme.colorScheme.surface)
  ) {
    items.forEachIndexed { index, navigationItem ->
      NavigationBarItem(
        selected = currentDestination == navigationItem.route/*index == selectedIndex*/,
        label = {
          Text(stringResource(id = navigationItem.resourceId))
        }, icon = {
          Icon(
            painterResource(id = navigationItem.icon),
            contentDescription = stringResource(id = navigationItem.resourceId),
            modifier = Modifier.size(24.dp)
          )
        },
        alwaysShowLabel = false,
        colors = NavigationBarItemColors(
          selectedIconColor = MaterialTheme.colorScheme.primary,
          selectedTextColor = MaterialTheme.colorScheme.primary,
          selectedIndicatorColor = Color.Transparent,
          disabledIconColor = Color.Gray,
          disabledTextColor = Color.Gray,
          unselectedIconColor = Color.Gray,
          unselectedTextColor = Color.Gray,
        ),
        onClick = {
          if (currentDestination != navigationItem.route) {
            //navController.navigate(navigationItem.route)
            navController.navigate(navigationItem.route) {
              popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
              }
              launchSingleTop = true
              restoreState = true
            }
          }
        })
    }
  }
}

@Composable
private fun MainScreenNavigationConfigurations(
  navController: NavHostController, paddingValues: PaddingValues
) {
  NavHost(
    navController = navController,
    startDestination = BottomNavigationScreens.Reciters.route,
    modifier = Modifier.padding(paddingValues)
  ) {
    composable(BottomNavigationScreens.Reciters.route) {
      RecitersScreen()
    }
    composable(BottomNavigationScreens.MyReciters.route) {
      MyRecitersScreen()
    }
  }
}

@Preview
@Composable
fun MainScreenPreview(modifier: Modifier = Modifier) {
  MainScreen()
}