package com.nedaluof.qurany.new_ui.screens.main.reciters

import android.content.Intent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nedaluof.qurany.data.model.Reciter
import com.nedaluof.qurany.new_ui.screens.main.common.LoadingView
import com.nedaluof.qurany.new_ui.screens.main.common.QuranySnackBar
import com.nedaluof.qurany.new_ui.screens.main.common.ReciterItem
import com.nedaluof.qurany.new_ui.screens.main.suras.SurasActivity
import com.nedaluof.qurany.new_ui.theme.QuranyComposeTheme
import com.nedaluof.qurany.util.AppConstants

/**
 * Created By NedaluOf - 5/31/2024.
 */
@Composable
fun RecitersListScreen(
  modifier: Modifier = Modifier,
  isMyRecitersScreen: Boolean = false,
  viewModel: RecitersViewModel = hiltViewModel(),
) {
  LaunchedEffect(isMyRecitersScreen) {
    viewModel.loadReciters(isMyRecitersScreen)
  }
  val context = LocalContext.current
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  when (uiState) {
    is RecitersUiState.Error -> QuranySnackBar(message = (uiState as RecitersUiState.Error).message)
    is RecitersUiState.Loading -> LoadingView()
    is RecitersUiState.Success -> RecitersList(
      modifier = modifier,
      items = (uiState as RecitersUiState.Success).reciters,
      onReciterClicked = { reciter ->
        with(context) {
          startActivity(
            Intent(this, SurasActivity::class.java).putExtra(AppConstants.RECITER_KEY, reciter)
          )
        }
      },
      onAddToFavoriteClicked = viewModel::processAddOrDeleteFromMyReciters
    )
  }
}

@Composable
fun RecitersList(
  modifier: Modifier = Modifier,
  items: List<Reciter>,
  onReciterClicked: (Reciter) -> Unit = {},
  onAddToFavoriteClicked: (Reciter) -> Unit = {}
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
    RecitersList(Modifier.fillMaxSize(), Reciter.mockList())
  }
}