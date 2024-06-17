package com.nedaluof.qurany.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Created By NedaluOf - 6/1/2024.
 */
@Composable
fun QuranySnackBar(
  message: String?,
  isError: Boolean = false,
  alignValue: Alignment = Alignment.BottomCenter
) {
  val snackBarHostState = remember { SnackbarHostState() }
  LaunchedEffect(key1 = message) {
    if (!message.isNullOrEmpty()) {
      snackBarHostState.showSnackbar(message)
    }
  }
  SnackbarHost(hostState = snackBarHostState) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(if (isError) Color.Red else MaterialTheme.colorScheme.primary)
    ) {
      Text(modifier = Modifier
        .fillMaxWidth()
        .graphicsLayer {
          shadowElevation = 5f
        }
        .padding(vertical = 16.dp)
        .align(alignValue),
        text = message ?: "",
        color = Color.White,
        fontSize = 14.sp,
        textAlign = TextAlign.Center)
    }
  }
}