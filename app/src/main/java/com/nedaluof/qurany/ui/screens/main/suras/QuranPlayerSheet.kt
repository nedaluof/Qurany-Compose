package com.nedaluof.qurany.ui.screens.main.suras

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.google.android.exoplayer2.ExoPlayer
import com.nedaluof.qurany.databinding.PlayerBottomSheetLayoutBinding

/**
 * Created By NedaluOf - 6/4/2024.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranyPlayerSheet(
  modifier: Modifier = Modifier,
  exoPlayer: ExoPlayer?,
  sheetState: SheetState,
  onClosePlayerClicked: () -> Unit,
  onDismissRequest: () -> Unit
) {
  ModalBottomSheet(
    modifier = modifier,
    sheetState = sheetState, onDismissRequest = onDismissRequest
  ) {
    AndroidViewBinding(PlayerBottomSheetLayoutBinding::inflate) {
      playerController.player = exoPlayer
      closeBtn.setOnClickListener {
        onClosePlayerClicked()
      }
    }
  }
}