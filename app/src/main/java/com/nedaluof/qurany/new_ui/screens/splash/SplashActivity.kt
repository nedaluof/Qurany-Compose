package com.nedaluof.qurany.new_ui.screens.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nedaluof.qurany.R
import com.nedaluof.qurany.new_ui.MainActivity
import com.nedaluof.qurany.new_ui.theme.QuranyComposeTheme
import com.nedaluof.qurany.util.getAppVersionName
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created By NedaluOf - 31/5/2024.
 */
@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

  private val splashViewModel by viewModels<SplashViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      CompositionLocalProvider(
        androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current,
      ) {
        val isNightModeEnabled = splashViewModel.isNightModeEnabled.collectAsStateWithLifecycle()
        QuranyComposeTheme(isNightModeEnabled.value) {
          SplashScreen()
        }
      }
    }
  }

  @Composable
  fun SplashScreen() {
    val versionLabel = stringResource(R.string.app_version)
    val versionName = LocalContext.current.getAppVersionName()
    Surface(modifier = Modifier.fillMaxSize()) {
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

  override fun onResume() {
    super.onResume()
    Handler(Looper.getMainLooper()).postDelayed(
      {
        startActivity(MainActivity.getIntent(this@SplashActivity))
        finish()
      }, 2200
    )
  }

  companion object {
    fun getIntent(
      context: Context
    ) = Intent(context, SplashActivity::class.java)
  }
}