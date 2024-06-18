package com.nedaluof.qurany.ui.screens.reciters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nedaluof.data.model.ReciterModel
import com.nedaluof.qurany.R
import com.nedaluof.qurany.ui.components.QuranyAlertDialog
import com.nedaluof.qurany.ui.components.QuranyLoadingView
import com.nedaluof.qurany.ui.components.QuranySearchBar
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
    is RecitersUiState.Loading -> QuranyLoadingView()
    is RecitersUiState.ShowReciter -> {
      RecitersList(modifier = modifier,
        viewModel = viewModel,
        isForFavorites = isForFavorites,
        onReciterClicked = onReciterClicked,
        onAddToFavoriteClicked = { reciter ->
          viewModel.reciterToBeProcessed = reciter
          if (reciter.isInMyFavorites) {
            showDeleteDialog = true
          } else {
            viewModel.processAddOrDeleteFromFavorites()
          }
        })
    }
  }

  when (operationsUiState) {
    is RecitersOperationsUiState.Error -> QuranySnackBar(message = (operationsUiState as RecitersOperationsUiState.Error).message)
    is RecitersOperationsUiState.Idl -> {}
    is RecitersOperationsUiState.Loading -> QuranyLoadingView()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecitersList(
  modifier: Modifier = Modifier,
  viewModel: RecitersViewModel,
  isForFavorites: Boolean = false,
  onReciterClicked: (ReciterModel) -> Unit = {},
  onAddToFavoriteClicked: (ReciterModel) -> Unit = {}
) {
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
  val searchText by viewModel.searchText.collectAsStateWithLifecycle()
  val recitersList by viewModel.recitersList.collectAsStateWithLifecycle()
  Scaffold(modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
    RecitersTopBar(
      searchIcon = if (isSearching) Icons.Default.Close else Icons.Default.Search,
      onSearchClickedClick = viewModel::toggleSearching,
      scrollBehavior = scrollBehavior
    )
  }) { paddingValues ->
    Column(modifier.padding(paddingValues)) {
      if (isSearching) {
        QuranySearchBar(
          modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
          searchQuery = searchText,
          onTextChange = viewModel::onSearchTextChange,
          placeHolder = stringResource(id = R.string.reciters_search_hint_label)
        )
      }
      recitersList?.let { list ->
        if (list.isNotEmpty()) {
          LazyColumn(
            modifier, contentPadding = PaddingValues(top = 10.dp, bottom = 30.dp)
          ) {
            items(count = list.size/*, key = { items[it].id ?: UUID.randomUUID() }*/) { index ->
              val item = list[index]
              ReciterItem(reciter = item, {
                onReciterClicked(item)
              }) {
                onAddToFavoriteClicked(item)
              }
            }
          }
        } else {
          if (isForFavorites || isSearching) {
            Box(modifier = Modifier.fillMaxSize()) {
              Text(
                stringResource(id = if (isForFavorites) R.string.no_favorite_reciters_label else R.string.no_reciters_search_label),
                modifier = Modifier
                  .align(Alignment.Center)
                  .padding(start = 18.dp, end = 18.dp),
                textAlign = TextAlign.Center
              )
            }
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecitersTopBar(
  searchIcon: ImageVector,
  onSearchClickedClick: () -> Unit,
  scrollBehavior: TopAppBarScrollBehavior
) {
  CenterAlignedTopAppBar(
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = MaterialTheme.colorScheme.primary,
      scrolledContainerColor = MaterialTheme.colorScheme.primary
    ),
    title = {
      Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
      ) {
        Text(
          text = stringResource(id = R.string.app_name),
          color = Color.White,
          style = MaterialTheme.typography.bodyLarge
        )
      }
    },
    modifier = Modifier.clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp)),
    actions = {
      IconButton(onClick = onSearchClickedClick) {
        Icon(
          searchIcon,
          contentDescription = stringResource(
            id = R.string.reciters_search_hint_label
          ), tint = Color.White
        )
      }
    },
    scrollBehavior = scrollBehavior
  )
}

@Preview
@Composable
fun RecitersScreenPreview(modifier: Modifier = Modifier) {
  QuranyComposeTheme {
    RecitersList(Modifier.fillMaxSize(), hiltViewModel<RecitersViewModel>())
  }
}