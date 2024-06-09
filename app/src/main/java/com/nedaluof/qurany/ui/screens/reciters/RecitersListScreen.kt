package com.nedaluof.qurany.ui.screens.reciters

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nedaluof.data.model.ReciterModel
import com.nedaluof.qurany.R
import com.nedaluof.qurany.ui.components.LoadingView
import com.nedaluof.qurany.ui.components.QuranyAlertDialog
import com.nedaluof.qurany.ui.components.QuranySnackBar
import com.nedaluof.qurany.ui.theme.QuranyComposeTheme

/**
 * Created By NedaluOf - 5/31/2024.
 */
@Composable
fun RecitersListScreen(
  modifier: Modifier = Modifier,
  isForFavorites: Boolean = false,
  viewModel: RecitersViewModel = hiltViewModel(),
  onReciterClicked: (ReciterModel) -> Unit = {}
) {
  LaunchedEffect(isForFavorites) {
    viewModel.loadReciters(isForFavorites)
  }
  val uiState by viewModel.recitersUiState.collectAsStateWithLifecycle()
  val operationsUiState by viewModel.recitersOperationUiState.collectAsStateWithLifecycle()
  var showDeleteDialog by remember { mutableStateOf(false) }
  when (uiState) {
    is RecitersUiState.Error -> QuranySnackBar(message = (uiState as RecitersUiState.Error).message)
    is RecitersUiState.Loading -> LoadingView()
    is RecitersUiState.Success -> {
      val items = (uiState as RecitersUiState.Success).reciters
      if (items.isNotEmpty()) {
        RecitersList(modifier = modifier,
          items = items,
          onReciterClicked = onReciterClicked,
          onAddToFavoriteClicked = { reciter ->
            viewModel.reciterToBeProcessed = reciter
            if (reciter.isInMyFavorites) {
              showDeleteDialog = true
            } else {
              viewModel.processAddOrDeleteFromFavorites()
            }
          })
      } else {
        Box(modifier = Modifier.fillMaxSize()) {
          Text(
            stringResource(id = R.string.no_favorite_reciters_label),
            modifier = Modifier
              .align(Alignment.Center)
              .padding(start = 18.dp, end = 18.dp),
            textAlign = TextAlign.Center
          )
        }
      }
    }
  }

  when (operationsUiState) {
    is RecitersOperationsUiState.Error -> QuranySnackBar(message = (operationsUiState as RecitersOperationsUiState.Error).message)
    is RecitersOperationsUiState.Idl -> {}
    is RecitersOperationsUiState.Loading -> LoadingView()
    is RecitersOperationsUiState.Success -> {
      val isDeleted = (operationsUiState as RecitersOperationsUiState.Success).isDeleted
      QuranySnackBar(message = stringResource(id = if (isDeleted) R.string.alrt_delete_success else R.string.alrt_add_success_msg))
    }
  }

  if (showDeleteDialog) {
    QuranyAlertDialog(
      onDismissRequest = {
        viewModel.reciterToBeProcessed = null
        showDeleteDialog = false
      },
      onConfirmation = {
        showDeleteDialog = false
        viewModel.processAddOrDeleteFromFavorites()
      },
      title = stringResource(id = R.string.alrt_delete_title),
      description = stringResource(
        id = R.string.alrt_delete_msg, viewModel.reciterToBeProcessed?.name ?: ""
      ),
      confirmationButtonTitle = stringResource(id = R.string.delete_label),
      dismissButtonTitle = stringResource(id = R.string.cancel_label),
      icon = Icons.Default.Delete.also { it.tintColor.red })
  }
}

@Composable
fun RecitersList(
  modifier: Modifier = Modifier,
  items: List<ReciterModel>,
  onReciterClicked: (ReciterModel) -> Unit = {},
  onAddToFavoriteClicked: (ReciterModel) -> Unit = {}
) {
  LazyColumn(
    modifier, contentPadding = PaddingValues(top = 10.dp, bottom = 30.dp)
  ) {
    items(count = items.size/*, key = { items[it].id ?: UUID.randomUUID() }*/) { index ->
      val item = items[index]
      ReciterItem(reciter = item, {
        onReciterClicked(item)
      }) {
        onAddToFavoriteClicked(item)
      }
    }
  }
}

@Preview
@Composable
fun RecitersScreenPreview(modifier: Modifier = Modifier) {
  QuranyComposeTheme {
    RecitersList(Modifier.fillMaxSize(), ReciterModel.mockList())
  }
}