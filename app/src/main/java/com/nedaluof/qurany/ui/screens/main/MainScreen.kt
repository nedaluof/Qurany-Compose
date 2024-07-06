package com.nedaluof.qurany.ui.screens.main

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nedaluof.data.model.ReciterModel
import com.nedaluof.qurany.R
import com.nedaluof.qurany.ui.navigation.BottomNavigationHost
import com.nedaluof.qurany.ui.navigation.BottomNavigationScreens
import com.nedaluof.qurany.ui.screens.MainViewModel
import com.nedaluof.qurany.ui.theme.AppGreen
import com.nedaluof.qurany.ui.theme.QuranyComposeTheme

/**
 * Created By NedaluOf - 5/31/2024.
 */
@Composable
fun MainScreen(
  modifier: Modifier = Modifier,
  viewModel: MainViewModel,
  onReciterClicked: (ReciterModel) -> Unit = {}
) {
  val navController = rememberNavController()
  Column {
    Toolbar(viewModel = viewModel)
    Scaffold(
      modifier = modifier, bottomBar = {
        BottomNavigationBar(navController = navController)
      }) { paddingValues ->
      BottomNavigationHost(
        navController = navController,
        paddingValues = paddingValues,
        onReciterClicked = onReciterClicked
      )
    }
  }
}

@Composable
fun Toolbar(
  modifier: Modifier = Modifier,
  viewModel: MainViewModel
) {
  val isCurrentLanguageEnglish by viewModel.isCurrentLanguageEnglish.collectAsStateWithLifecycle()
  LaunchedEffect(isCurrentLanguageEnglish) {
    AppCompatDelegate.setApplicationLocales(
      LocaleListCompat.forLanguageTags(
        if (isCurrentLanguageEnglish) "en" else "ar"
      )
    )
  }
  Row(
    modifier
      .fillMaxWidth()
      .background(AppGreen)
  ) {
    TextButton(
      onClick = viewModel::changeAppLanguage
    ) {
      Text(
        if (isCurrentLanguageEnglish) "العربية" else "EN", color = Color.White
      )
    }
    IconButton(
      onClick = viewModel::changeDayNightMode,
    ) {
      Icon(
        painter = painterResource(id = if (viewModel.isNightModeEnabled.value) R.drawable.ic_light_mode else R.drawable.ic_night_mode),
        contentDescription = "change app theme between light and night",
        tint = Color.Unspecified
      )
    }
  }
}

@Composable
private fun BottomNavigationBar(
  navController: NavHostController
) {
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentDestination = navBackStackEntry?.destination?.route ?: "reciters"
  NavigationBar(
    modifier = Modifier
      .clip(RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp))
      .border(
        width = 1.5.dp,
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp)
      ),
    containerColor = MaterialTheme.colorScheme.background
  ) {
    listOf(
      BottomNavigationScreens.Reciters,
      BottomNavigationScreens.MyReciters
    ).forEach { navigationItem ->
      NavigationBarItem(selected = currentDestination == navigationItem.route, label = {
        Text(stringResource(id = navigationItem.resourceId))
      },
        icon = {
          Icon(
            painterResource(id = navigationItem.icon),
            contentDescription = stringResource(id = navigationItem.resourceId),
            modifier = Modifier.size(24.dp)
          )
        }, alwaysShowLabel = false, colors = NavigationBarItemColors(
          selectedIconColor = MaterialTheme.colorScheme.primary,
          selectedTextColor = MaterialTheme.colorScheme.primary,
          selectedIndicatorColor = Color.Transparent,
          disabledIconColor = Color.Gray,
          disabledTextColor = Color.Gray,
          unselectedIconColor = Color.Gray,
          unselectedTextColor = Color.Gray,
        ), onClick = {
          if (currentDestination != navigationItem.route) {
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


@Preview
@Composable
fun MainScreenPreview(modifier: Modifier = Modifier) {
  QuranyComposeTheme {
    MainScreen(viewModel = hiltViewModel<MainViewModel>())
  }
}