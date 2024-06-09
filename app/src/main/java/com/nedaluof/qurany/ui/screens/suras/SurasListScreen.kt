package com.nedaluof.qurany.ui.screens.suras

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.nedaluof.data.model.ReciterModel
import com.nedaluof.data.model.SuraModel
import com.nedaluof.qurany.R
import com.nedaluof.qurany.databinding.PlayerBottomSheetLayoutBinding
import com.nedaluof.qurany.ui.theme.QuranyComposeTheme
import com.nedaluof.qurany.util.isNetworkOk
import com.nedaluof.qurany.util.postDelayed
import kotlinx.coroutines.launch

/**
 * Created By NedaluOf - 6/3/2024.
 */

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurasListScreen(
  modifier: Modifier = Modifier,
  reciter: ReciterModel,
  exoPlayer: ExoPlayer? = null,
  surasViewModel: SurasViewModel = hiltViewModel(),
  onPlayClicked: (SuraModel) -> Unit,
  onDownloadClicked: (SuraModel) -> Unit,
  stopPlaying: () -> Unit,
  onBackPressed: () -> Unit
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
    bottomSheetState = rememberStandardBottomSheetState(
      initialValue = SheetValue.Expanded
    )
  )
  val suraToPlay by remember { surasViewModel.currentPlayingSura }
  BottomSheetScaffold(
    modifier = modifier,
    sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    sheetPeekHeight = if (suraToPlay == null) 0.dp else 60.dp,
    sheetDragHandle = null,
    scaffoldState = bottomSheetScaffoldState,
    sheetContent = {
      if (suraToPlay != null) {
        AndroidViewBinding(PlayerBottomSheetLayoutBinding::inflate) {
          playerController.player = exoPlayer
          exoPlayer?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
              super.onPlaybackStateChanged(playbackState)
              if (playbackState == Player.STATE_ENDED) {
                { surasViewModel.currentPlayingSura.value = null }.postDelayed(1500)
              }
            }
          })
          closeBtn.setOnClickListener {
            surasViewModel.currentPlayingSura.value = null
            stopPlaying()
          }
          reciterSuraName.text = suraToPlay?.playerTitle ?: ""
          coroutineScope.launch {
            if (suraToPlay?.isSuraExistInLocalStorage == false) {
              if (context.isNetworkOk()) {
                bottomSheetScaffoldState.bottomSheetState.expand()
              } else {
                bottomSheetScaffoldState.bottomSheetState.partialExpand()
              }
            } else {
              bottomSheetScaffoldState.bottomSheetState.expand()
            }
          }
        }
      }
    }) {
    SurasList(reciter = reciter, onPlayClicked = { sura ->
      coroutineScope.launch {
        bottomSheetScaffoldState.bottomSheetState.expand()
      }
      surasViewModel.currentPlayingSura.value = sura
      onPlayClicked(sura)
    }, onDownloadClicked = onDownloadClicked, onCloseClicked = onBackPressed, onScrolled = {
      coroutineScope.launch {
        bottomSheetScaffoldState.bottomSheetState.partialExpand()
      }
    })
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurasList(
  modifier: Modifier = Modifier,
  reciter: ReciterModel,
  viewModel: SurasViewModel = hiltViewModel(),
  onPlayClicked: (SuraModel) -> Unit,
  onDownloadClicked: (SuraModel) -> Unit,
  onCloseClicked: () -> Unit = {},
  onScrolled: () -> Unit = {},
) {
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  Scaffold(modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
    SurasTopBar(
      reciterName = reciter.name,
      scrollBehavior = scrollBehavior,
      onCloseClicked = onCloseClicked
    )
  }) { paddingValues ->
    val state = rememberLazyListState()
    LazyColumn(
      modifier = modifier
        .padding(paddingValues)
        .fillMaxHeight(),
      state = state,
      contentPadding = PaddingValues(top = 10.dp, bottom = 60.dp)
    ) {
      val items = viewModel.loadReciterSuras(reciter)
      items(count = items.size, key = { items[it].id }) { index ->
        val item = items[index]
        SuraItem(sura = item,
          onPlayClicked = { onPlayClicked(item) },
          onDownloadClicked = { onDownloadClicked(item) })
      }
    }

    LaunchedEffect(state.isScrollInProgress) {
      onScrolled()
    }
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
    navigationIcon = {
      IconButton(
        onClick = onCloseClicked
      ) {
        Icon(
          painter = painterResource(id = R.drawable.ic_back_arrow),
          contentDescription = "close suras screen",
          tint = Color.White
        )
      }
    },
    modifier = modifier.clip(RoundedCornerShape(bottomEnd = 20.dp)),
    scrollBehavior = scrollBehavior
  )
}

@Preview
@Composable
fun SurasListPreview(modifier: Modifier = Modifier) {
  QuranyComposeTheme {
    SurasList(
      reciter = ReciterModel.mockList()[0],
      onPlayClicked = {},
      onDownloadClicked = {},
    )
  }
}