package com.nedaluof.qurany.new_ui.screens.main.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Created By NedaluOf - 6/1/2024.
 */

@Composable
fun LoadingView() {
  Box(
    contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
  ) {
    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
  }
}