package com.nedaluof.qurany.ui.features.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.nedaluof.qurany.R
import com.nedaluof.qurany.ui.navigation.QuranyScreens
import com.nedaluof.qurany.ui.theme.QuranyTheme
import com.nedaluof.qurany.util.appVersionName
import kotlinx.coroutines.delay

/**
 * Created By NedaluOf - 31/5/2024.
 */
@Composable
fun SplashScreen(
  modifier: Modifier = Modifier,
  navController: NavController
) {
  var isSplashShown by remember { mutableStateOf(false) }
  val versionLabel = stringResource(R.string.app_version, LocalContext.current.appVersionName())
  LaunchedEffect(isSplashShown) {
    if (!isSplashShown) {
      delay(2200L)
      isSplashShown = true
    }
  }
  if (isSplashShown) {
    navController.navigate(route = QuranyScreens.HOME.route)
  }

  Surface(
    modifier = modifier.fillMaxSize()
  ) {
    Box {
      Image(
        painterResource(id = R.drawable.ic_qurany),
        contentDescription = "app logo",
        modifier = Modifier.align(Alignment.Center)
      )
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(end = 20.dp)
          .clip(RoundedCornerShape(topEnd = 50.dp))
          .background(color = MaterialTheme.colorScheme.primary)
          .align(Alignment.BottomCenter),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = stringResource(id = R.string.app_powered_by),
          style = MaterialTheme.typography.bodyLarge,
          color = Color.White,
          modifier = Modifier.padding(top = 12.dp)
        )
        Text(
          text = versionLabel,
          style = MaterialTheme.typography.bodyLarge,
          color = Color.White,
          modifier = Modifier.padding(top = 8.dp, bottom = 10.dp)
        )
      }
    }
  }
}

@Preview
@Composable
fun SplashScreenPreview() {
  QuranyTheme { SplashScreen(navController = rememberNavController()) }
}