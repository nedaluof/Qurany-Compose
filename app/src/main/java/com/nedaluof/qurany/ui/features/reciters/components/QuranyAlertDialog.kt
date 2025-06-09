package com.nedaluof.qurany.ui.features.reciters.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Created By NedaluOf - 6/3/2024.
 */
@Composable
fun QuranyAlertDialog(
  onDismissRequest: () -> Unit,
  onConfirmation: () -> Unit,
  title: String,
  description: String,
  confirmationButtonTitle: String,
  dismissButtonTitle: String,
  icon: ImageVector
) {
  AlertDialog(icon = {
    Icon(icon, contentDescription = "Example Icon")
  }, title = {
    Text(text = title)
  }, text = {
    Text(text = description)
  }, onDismissRequest = onDismissRequest, confirmButton = {
    TextButton(
      onClick = onConfirmation
    ) {
      Text(confirmationButtonTitle)
    }
  }, dismissButton = {
    TextButton(onClick = onDismissRequest) {
      Text(dismissButtonTitle)
    }
  })
}