package com.nedaluof.qurany.ui.features.home

import android.app.LocaleManager
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.nedaluof.qurany.ui.navigation.HomeBottomNavHost
import com.nedaluof.qurany.ui.navigation.HomeBottomNavScreens
import com.nedaluof.qurany.ui.theme.QuranyTheme

/**
 * Created By NedaluOf - 5/31/2024.
 */
@Composable
fun HomeScreen(
  modifier: Modifier = Modifier,
  viewModel: HomeViewModel = hiltViewModel(),
  onReciterClicked: (ReciterModel) -> Unit = {}
) {
  val navController = rememberNavController()
  val isCurrentLanguageEnglish by viewModel.isCurrentLanguageEnglish.collectAsStateWithLifecycle()
  val isNightModeEnabled by viewModel.isNightModeEnabled.collectAsStateWithLifecycle()
  Scaffold(
    modifier = modifier
      .fillMaxSize(),
    topBar = {
      QuranyTobBar(
        isNightModeEnabled = isNightModeEnabled,
        isCurrentLanguageEnglish = isCurrentLanguageEnglish,
        onChangeDayNightMode = viewModel::changeDayNightMode,
        onChangeAppLanguage = viewModel::changeAppLanguage
      )
    },
    bottomBar = {
      BottomNavigationBar(navController = navController)
    }
  ) { paddingValues ->
    Box(modifier = Modifier.padding(paddingValues)) {
      HomeBottomNavHost(
        navController = navController,
        onReciterClicked = onReciterClicked
      )
    }
  }
}

@Composable
fun QuranyTobBar(
  modifier: Modifier = Modifier,
  isCurrentLanguageEnglish: Boolean = true,
  isNightModeEnabled: Boolean = false,
  onChangeDayNightMode: () -> Unit = {},
  onChangeAppLanguage: () -> Unit = {},
) {
  val context = LocalContext.current
  Box(
    modifier = modifier
      .background(MaterialTheme.colorScheme.primary)
  ) {
    Row(
      modifier = Modifier
        .displayCutoutPadding()
        .fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      TextButton(
        onClick = {
          val languageCode = if (isCurrentLanguageEnglish) "ar" else "en"
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java)
              .applicationLocales = LocaleList.forLanguageTags(languageCode)
          } else {
            AppCompatDelegate.setApplicationLocales(
              LocaleListCompat.forLanguageTags(languageCode)
            )
          }
          onChangeAppLanguage()
        }
      ) {
        Text(
          if (isCurrentLanguageEnglish) "العربية" else "EN", color = Color.White
        )
      }
      IconButton(
        onClick = onChangeDayNightMode,
      ) {
        Icon(
          painter = painterResource(id = if (isNightModeEnabled) R.drawable.ic_light_mode else R.drawable.ic_night_mode),
          contentDescription = "change app theme between light and night",
          tint = Color.Unspecified
        )
      }
    }
  }
}

@Composable
private fun BottomNavigationBar(
  modifier: Modifier = Modifier,
  navController: NavHostController
) {
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentDestination = navBackStackEntry?.destination?.route ?: "reciters"
  NavigationBar(
    modifier = modifier
      .clip(RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp))
      .border(
        width = 1.5.dp,
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp)
      )
      .navigationBarsPadding(),
    containerColor = MaterialTheme.colorScheme.background
  ) {
    listOf(
      HomeBottomNavScreens.Reciters,
      HomeBottomNavScreens.MyReciters
    ).forEach { navigationItem ->
      NavigationBarItem(
        selected = currentDestination == navigationItem.route, label = {
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
fun MainScreenPreview() {
  QuranyTheme { HomeScreen(viewModel = hiltViewModel()) }
}