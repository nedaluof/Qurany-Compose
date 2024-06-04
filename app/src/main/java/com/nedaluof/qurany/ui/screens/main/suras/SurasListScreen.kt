package com.nedaluof.qurany.ui.screens.main.suras

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nedaluof.qurany.R
import com.nedaluof.qurany.data.model.Reciter
import com.nedaluof.qurany.data.model.SuraModel
import com.nedaluof.qurany.ui.theme.QuranyComposeTheme

/**
 * Created By NedaluOf - 6/3/2024.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurasListScreen(
  modifier: Modifier = Modifier,
  reciter: Reciter,
  viewModel: SurasViewModel = hiltViewModel(),
  onPlayClicked: (SuraModel) -> Unit,
  onDownloadClicked: (SuraModel) -> Unit,
  onCloseClicked: () -> Unit = {}
) {
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  Scaffold(modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
    SurasTopBar(
      reciterName = reciter.name ?: "",
      scrollBehavior = scrollBehavior,
      onCloseClicked = onCloseClicked
    )
  }) { paddingValues ->
    SurasList(
      paddingValues = paddingValues,
      items = viewModel.loadReciterSuras(reciter),
      onPlayClicked = onPlayClicked,
      onDownloadClicked = onDownloadClicked
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurasTopBar(
  modifier: Modifier = Modifier,
  reciterName: String,
  scrollBehavior: TopAppBarScrollBehavior,
  onCloseClicked: () -> Unit
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
          text = reciterName, color = Color.White, style = MaterialTheme.typography.bodyLarge
        )
      }
    },
    actions = {
      IconButton(onClick = onCloseClicked) {
        Icon(
          painter = painterResource(id = R.drawable.ic_search_2),
          contentDescription = "close suras screen",
          tint = Color.White
        )
      }
    },
    modifier = modifier.clip(RoundedCornerShape(bottomEnd = 20.dp)),
    scrollBehavior = scrollBehavior
  )
}

@Composable
fun SurasList(
  modifier: Modifier = Modifier,
  paddingValues: PaddingValues,
  items: List<SuraModel>,
  onPlayClicked: (SuraModel) -> Unit,
  onDownloadClicked: (SuraModel) -> Unit
) {
  LazyColumn(
    modifier = modifier.padding(paddingValues),
    contentPadding = PaddingValues(top = 10.dp, bottom = 40.dp)
  ) {
    items(count = items.size, key = { items[it].id }) { index ->
      val item = items[index]
      SuraItem(sura = item,
        onPlayClicked = { onPlayClicked(item) },
        onDownloadClicked = { onDownloadClicked(item) })
    }
  }
}

@Preview
@Composable
fun SurasListPreview(modifier: Modifier = Modifier) {
  QuranyComposeTheme {
    SurasList(
      paddingValues = PaddingValues(0.dp),
      items = SuraModel.mockList(),
      onPlayClicked = {},
      onDownloadClicked = {},
    )
  }
}