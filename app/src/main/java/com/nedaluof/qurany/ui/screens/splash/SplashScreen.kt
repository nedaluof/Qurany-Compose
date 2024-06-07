package com.nedaluof.qurany.ui.screens.splash

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nedaluof.qurany.R
import com.nedaluof.qurany.ui.theme.QuranyComposeTheme

/**
 * Created By NedaluOf - 31/5/2024.
 */
@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
  val context = LocalContext.current
  val versionLabel = stringResource(R.string.app_version)
  val versionName = context.packageManager.getPackageInfo(context.packageName, 0).versionName
  Surface(modifier = modifier.fillMaxSize()) {
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
          text = "$versionLabel $versionName",
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
fun SplashScreenPreview(modifier: Modifier = Modifier) {
  QuranyComposeTheme {
    SplashScreen()
  }
}