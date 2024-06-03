package com.nedaluof.qurany.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nedaluof.qurany.R
import com.nedaluof.qurany.ui.navigation.BottomNavigationScreens
import com.nedaluof.qurany.ui.screens.main.reciters.RecitersListScreen
import com.nedaluof.qurany.ui.theme.AppGreen
import com.nedaluof.qurany.ui.theme.QuranyComposeTheme

/**
 * Created By NedaluOf - 5/31/2024.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
  modifier: Modifier = Modifier,
  viewModel: MainViewModel,
  reloadApp: () -> Unit = {}
) {
  val navController = rememberNavController()
  val bottomNavigationItems = listOf(
    BottomNavigationScreens.Reciters, BottomNavigationScreens.MyReciters
  )
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  Column {
    Toolbar(viewModel = viewModel, reloadApp = reloadApp)
    Scaffold(modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
      TopAppBar(
        navController = navController, onSearchClickedClick = {}, scrollBehavior = scrollBehavior
      )
    }, bottomBar = {
      BottomNavigationBar(navController = navController, items = bottomNavigationItems)
    }) { paddingValues ->
      MainScreenNavigationConfigurations(
        navController = navController,
        paddingValues = paddingValues
      )
    }
  }
}

@Composable
fun Toolbar(
  modifier: Modifier = Modifier,
  viewModel: MainViewModel,
  reloadApp: () -> Unit
) {
  Row(
    modifier
      .fillMaxWidth()
      .background(AppGreen)
  ) {
    TextButton(
      onClick = {
        viewModel.changeAppLanguage()
        reloadApp()
      }
    ) {
      Text(viewModel.appLanguageEnglish.value, color = Color.White)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(
  navController: NavHostController,
  onSearchClickedClick: () -> Unit,
  scrollBehavior: TopAppBarScrollBehavior
) {
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentDestination = navBackStackEntry?.destination?.route ?: "reciters"
  CenterAlignedTopAppBar(
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = MaterialTheme.colorScheme.primary,
      scrolledContainerColor = MaterialTheme.colorScheme.primary
    ),
    title = {
      Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
      ) {
        Text(
          text = stringResource(id = R.string.app_name),
          color = Color.White,
          style = MaterialTheme.typography.bodyLarge
        )
      }
    },
    modifier = Modifier.clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp)),
    actions = {
      if (currentDestination == BottomNavigationScreens.Reciters.route) {
        IconButton(
          onClick = onSearchClickedClick,
        ) {
          Icon(
            painter = painterResource(id = R.drawable.ic_search_2),
            contentDescription = stringResource(
              id = R.string.reciters_search_hint
            ),
            tint = Color.White
          )
        }
      }
    },
    scrollBehavior = scrollBehavior
  )
}

@Composable
private fun BottomNavigationBar(
  navController: NavHostController, items: List<BottomNavigationScreens>
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
  ) {
    items.forEach { navigationItem ->
      NavigationBarItem(selected = currentDestination == navigationItem.route, label = {
        Text(stringResource(id = navigationItem.resourceId))
      }, icon = {
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
      RecitersListScreen(Modifier.fillMaxSize())
    }
    composable(BottomNavigationScreens.MyReciters.route) {
      RecitersListScreen(Modifier.fillMaxSize(), true)
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