package com.nedaluof.qurany.new_ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import com.nedaluof.qurany.new_ui.screens.main.MainScreen
import com.nedaluof.qurany.new_ui.theme.QuranyComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      CompositionLocalProvider(
        androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current,
      ) {
        QuranyComposeTheme() {
          MainScreen()
        }
      }
    }
  }

  companion object {
    fun getIntent(
      context: Context
    ) = Intent(context, MainActivity::class.java)
  }
}