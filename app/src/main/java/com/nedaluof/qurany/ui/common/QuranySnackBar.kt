package com.nedaluof.qurany.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Created By NedaluOf - 6/1/2024.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranySnackBar(
  message: String?,
  isError: Boolean = false,
  alignValue: Alignment = Alignment.BottomCenter,
  shape: Shape = RoundedCornerShape(topEnd = 8.dp, topStart = 8.dp),
  offsetYValue: Int = 10
) {
  val snackBarHostState = remember { SnackbarHostState() }
  LaunchedEffect(key1 = message) {
    if (!message.isNullOrEmpty()) {
      snackBarHostState.showSnackbar(message)
    }
  }

  val swipeState = rememberSwipeToDismissBoxState()
  SnackbarHost(
    hostState = snackBarHostState
  ) { data ->
    SwipeToDismissBox(
      modifier = Modifier.offset(y = offsetYValue.dp),
      state = swipeState, backgroundContent = {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
        )
      }
    ) {
      Box(
        modifier = Modifier.fillMaxSize()
      ) {
        Text(
          modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
              shadowElevation = 5f
            }
            .background(
              if (isError) Color.Red else MaterialTheme.colorScheme.primary,
              shape = shape
            )
            .padding(vertical = 16.dp)
            .align(alignValue),
          text = data.visuals.message,
          color = Color.White,
          fontSize = 14.sp,
          textAlign = TextAlign.Center)
      }
    }
  }
}