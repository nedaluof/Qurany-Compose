package com.nedaluof.qurany.ui.features.reciters

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nedaluof.data.model.ReciterModel
import com.nedaluof.qurany.R
import com.nedaluof.qurany.ui.common.QuranySearchBar
import com.nedaluof.qurany.ui.features.reciters.components.QuranyAlertDialog
import com.nedaluof.qurany.ui.features.reciters.components.QuranyLoadingView
import com.nedaluof.qurany.ui.features.reciters.components.QuranySnackBar
import com.nedaluof.qurany.ui.features.reciters.components.ReciterItem
import com.nedaluof.qurany.ui.theme.QuranyTheme

/**
 * Created By NedaluOf - 5/31/2024.
 */
@Composable
fun RecitersScreen(
  modifier: Modifier = Modifier,
  isForFavorites: Boolean = false,
  viewModel: RecitersViewModel = hiltViewModel(),
  onReciterClicked: (reciterId: Int) -> Unit = {}
) {
  LaunchedEffect(isForFavorites) {
    viewModel.loadReciters(isForFavorites)
  }
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  var reciterSelectedToDelete by remember { mutableStateOf<ReciterModel?>(null) }

  uiState.errorMessage?.let { message ->
    QuranySnackBar(message = message)
  }

  if (uiState.showLoading) {
    QuranyLoadingView()
  }

  uiState.reciters?.let { reciters ->
    RecitersList(
      modifier = modifier,
      recitersList = reciters,
      searchText = uiState.searchQuery,
      isSearching = uiState.isSearching,
      isForFavorites = isForFavorites,
      onReciterClicked = onReciterClicked,
      onSearchTextChange = viewModel::onSearchTextChange,
      onToggleSearchingBarRequested = viewModel::toggleSearching,
      onAddToFavoriteClicked = { reciter ->
        if (reciter.isInMyFavorites) {
          reciterSelectedToDelete = reciter
        } else {
          viewModel.addReciterToFavorites(reciter)
        }
      }
    )
  }

  if (uiState.isDeletedFromFavorites == true || uiState.isAddedToFavorites == true) {
    val isDeleted = uiState.isDeletedFromFavorites == true
    QuranySnackBar(message = stringResource(id = if (isDeleted) R.string.alert_process_success_label else R.string.alert_add_to_favorites_success_label))
  }

  reciterSelectedToDelete?.let {
    QuranyAlertDialog(
      onDismissRequest = {
        reciterSelectedToDelete = null
      },
      onConfirmation = {
        reciterSelectedToDelete?.let(viewModel::deleteReciterFromFavorites)
        reciterSelectedToDelete = null
      },
      title = stringResource(id = R.string.alert_remove_from_favorites_title_label),
      description = stringResource(
        id = R.string.alert_remove_from_favorites_description_label,
        reciterSelectedToDelete?.name ?: ""
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
  isSearching: Boolean = false,
  isForFavorites: Boolean = false,
  searchText: String = "",
  recitersList: List<ReciterModel> = emptyList(),
  onToggleSearchingBarRequested: () -> Unit = {},
  onReciterClicked: (reciterId: Int) -> Unit = {},
  onSearchTextChange: (String) -> Unit = {},
  onAddToFavoriteClicked: (ReciterModel) -> Unit = {}
) {
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  Scaffold(
    modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
    topBar = {
      RecitersTopBar(
        isSearching = isSearching,
        onSearchClickedClick = onToggleSearchingBarRequested,
        scrollBehavior = scrollBehavior
      )
    }) { paddingValues ->
    Column(Modifier.padding(paddingValues)) {
      if (isSearching) {
        QuranySearchBar(
          modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
          searchQuery = searchText,
          onTextChange = onSearchTextChange,
          placeHolder = stringResource(id = R.string.reciters_search_hint_label)
        )
      }
      if (recitersList.isNotEmpty()) {
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(top = 10.dp, bottom = 30.dp)
        ) {
          items(count = recitersList.size/*, key = { items[it].id ?: UUID.randomUUID() }*/) { index ->
            val item = recitersList[index]
            ReciterItem(reciter = item, {
              onReciterClicked(item.id)
            }) {
              onAddToFavoriteClicked(item)
            }
          }
        }
      } else {
        if (isForFavorites || isSearching) {
          Box(modifier = Modifier.fillMaxSize()) {
            Text(
              stringResource(id = if (isForFavorites) R.string.no_favorite_reciters_message else R.string.no_reciters_search_label),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecitersTopBar(
  isSearching: Boolean,
  onSearchClickedClick: () -> Unit,
  scrollBehavior: TopAppBarScrollBehavior
) {
  CenterAlignedTopAppBar(
    modifier = Modifier
      .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp)),
    scrollBehavior = scrollBehavior,
    windowInsets = WindowInsets(0),
    title = {
      Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(id = R.string.app_name),
        color = Color.White,
        textAlign = TextAlign.Center,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium
      )
    },
    actions = {
      IconButton(
        modifier = Modifier.padding(horizontal = 16.dp),
        onClick = onSearchClickedClick
      ) {
        Icon(
          imageVector = if (isSearching) Icons.Default.Close else Icons.Default.Search,
          contentDescription = null,
          tint = Color.White
        )
      }
    },
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = MaterialTheme.colorScheme.primary,
      scrolledContainerColor = MaterialTheme.colorScheme.primary
    )
  )
}

@Preview
@Composable
fun RecitersScreenPreview() {
  QuranyTheme { RecitersList() }
}