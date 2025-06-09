package com.nedaluof.qurany.ui.features.suras

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.nedaluof.data.model.ReciterModel
import com.nedaluof.data.model.SuraModel
import com.nedaluof.qurany.R
import com.nedaluof.qurany.databinding.PlayerBottomSheetLayoutBinding
import com.nedaluof.qurany.service.QuranyDownloadService
import com.nedaluof.qurany.ui.common.QuranySearchBar
import com.nedaluof.qurany.ui.features.reciters.components.QuranySnackBar
import com.nedaluof.qurany.ui.features.suras.components.SuraItem
import com.nedaluof.qurany.ui.features.suras.components.rememberManagedMediaController
import com.nedaluof.qurany.ui.theme.QuranyTheme
import com.nedaluof.qurany.util.isInternetAvailable
import com.nedaluof.qurany.util.toast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created By NedaluOf - 6/3/2024.
 */
@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurasScreen(
  modifier: Modifier = Modifier,
  reciterId: Int,
  viewModel: SurasViewModel = hiltViewModel(),
  onBackPressed: () -> Unit
) {
  val context = LocalContext.current
  val suraToPlay by remember { viewModel.currentPlayingSura }
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val coroutineScope = rememberCoroutineScope()
  val mediaController by rememberManagedMediaController()
  val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
    bottomSheetState = rememberStandardBottomSheetState(
      initialValue = SheetValue.Expanded
    )
  )
  LaunchedEffect(suraToPlay) {
    suraToPlay?.let { sura ->
      val isLocal = viewModel.isSuraExistInLocalStorage(sura.suraSubPath)
      val suraURI = if (isLocal) sura.suraLocalPath.toUri() else sura.suraUrl.toUri()
      if (!isLocal) {
        if (!context.isInternetAvailable()) {
          context.toast(R.string.alert_no_internet_message)
          return@LaunchedEffect
        }
      }

      val mediaItem =
        MediaItem.Builder()
          .setMediaId("${suraToPlay?.id}")
          .setUri(suraURI)
          .setMediaMetadata(
            MediaMetadata.Builder().setDisplayTitle(context.getString(R.string.app_name))
              .setTitle(context.getString(R.string.app_name)).setArtist(suraToPlay?.reciterName)
              .setTitle(suraToPlay?.playerTitle).build()
          ).build()
      mediaController?.run {
        setMediaItem(mediaItem)
        prepare()
        play()
      }
      context.toast(
        if (isLocal) R.string.alert_sura_playing_locally_message else R.string.alert_sura_playing_online_message
      )
    } ?: run {
      mediaController?.let {
        if (it.isPlaying || it.isLoading) {
          it.stop()
        }
      }
    }
  }

  LaunchedEffect(Unit) { viewModel.loadReciterSuras(reciterId) }

  BottomSheetScaffold(
    modifier = modifier,
    sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    sheetPeekHeight = if (suraToPlay == null) 0.dp else 60.dp,
    sheetDragHandle = null,
    scaffoldState = bottomSheetScaffoldState,
    sheetContent = {
      if (suraToPlay != null) {
        AndroidViewBinding(PlayerBottomSheetLayoutBinding::inflate) {
          playerController.player = mediaController
          mediaController?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
              super.onPlaybackStateChanged(playbackState)
              if (playbackState == Player.STATE_ENDED) {
                coroutineScope.launch {
                  delay(1000)
                  viewModel.currentPlayingSura.value = null
                }
              }
            }
          })
          closeBtn.setOnClickListener {
            viewModel.currentPlayingSura.value = null
          }
          reciterSuraName.text = suraToPlay?.playerTitle ?: ""
          coroutineScope.launch {
            val isLocalSura =
              viewModel.isSuraExistInLocalStorage(suraToPlay?.suraSubPath ?: "")
            if (!isLocalSura) {
              if (context.isInternetAvailable()) {
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
    SurasScreenContent(
      reciterName = uiState.reciterName,
      surasList = uiState.suras,
      isInFavorites = uiState.isReciterInFavorites,
      isSearching = uiState.isSearching,
      searchText = uiState.searchQuery,
      onPlayClicked = { sura ->
        coroutineScope.launch {
          bottomSheetScaffoldState.bottomSheetState.expand()
        }
        viewModel.currentPlayingSura.value = sura
      },
      onDownloadClicked = { sura ->
        with(context) {
          startService(QuranyDownloadService.getIntent(this, sura))
        }
      },
      onCloseClicked = onBackPressed,
      onFavoriteClicked = viewModel::addOrDeleteFromFavorites,
      onSearchClickedClick = viewModel::toggleSearching,
      onSearchTextChange = viewModel::onSearchTextChange,
      onScrolled = {
        coroutineScope.launch {
          bottomSheetScaffoldState.bottomSheetState.partialExpand()
        }
      }
    )
  }

  uiState.errorMessage?.let { message ->
    QuranySnackBar(
      message = message,
      offsetYValue = 100,
      shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
    )
  }

  if (uiState.isDeletedFromFavorites == true || uiState.isAddedToFavorites == true) {
    val isDeleted = uiState.isDeletedFromFavorites == true
    QuranySnackBar(
      message = stringResource(id = if (isDeleted) R.string.alert_process_success_label else R.string.alert_add_to_favorites_success_label)
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurasScreenContent(
  modifier: Modifier = Modifier,
  reciterName: String = "",
  surasList: List<SuraModel> = emptyList(),
  isInFavorites: Boolean = false,
  isSearching: Boolean = false,
  searchText: String = "",
  onSearchTextChange: (String) -> Unit = {},
  onFavoriteClicked: () -> Unit = {},
  onPlayClicked: (SuraModel) -> Unit = {},
  onDownloadClicked: (SuraModel) -> Unit = {},
  onCloseClicked: () -> Unit = {},
  onScrolled: () -> Unit = {},
  onSearchClickedClick: () -> Unit = {},
) {
  val lazyListState = rememberLazyListState()
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  LaunchedEffect(lazyListState.isScrollInProgress) {
    onScrolled()
  }

  Scaffold(
    modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      SurasTopBar(
        reciterName = reciterName,
        isInFavorites = isInFavorites,
        isSearching = isSearching,
        scrollBehavior = scrollBehavior,
        onCloseClicked = onCloseClicked,
        onFavoriteClicked = onFavoriteClicked,
        onSearchClickedClick = onSearchClickedClick
      )
    }) { paddingValues ->
    Column(
      Modifier
        .padding(paddingValues)
        .fillMaxSize()
    ) {
      if (isSearching) {
        QuranySearchBar(
          modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
          searchQuery = searchText,
          onTextChange = onSearchTextChange,
          placeHolder = stringResource(id = R.string.reciter_suras_search_hint_label)
        )
      }
      if (surasList.isNotEmpty()) {
        LazyColumn(
          modifier = Modifier.fillMaxHeight(),
          state = lazyListState,
          contentPadding = PaddingValues(top = 10.dp, bottom = 60.dp)
        ) {
          items(count = surasList.size, key = { surasList[it].id }) { index ->
            val item = surasList[index]
            SuraItem(
              sura = item,
              onPlayClicked = { onPlayClicked(item) },
              onDownloadClicked = { onDownloadClicked(item) })
          }
        }
      } else {
        if (isSearching) {
          Box(modifier = Modifier.fillMaxSize()) {
            Text(
              modifier = Modifier
                .align(Alignment.Center)
                .padding(start = 18.dp, end = 18.dp),
              text = stringResource(id = R.string.no_suras_match_search_query_message),
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
fun SurasTopBar(
  modifier: Modifier = Modifier,
  reciterName: String,
  scrollBehavior: TopAppBarScrollBehavior,
  isInFavorites: Boolean,
  isSearching: Boolean,
  onSearchClickedClick: () -> Unit,
  onFavoriteClicked: () -> Unit,
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
          text = reciterName,
          color = Color.White,
          fontSize = 18.sp,
          fontWeight = FontWeight.Medium
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
    actions = {
      IconButton(
        modifier = Modifier.padding(start = 16.dp),
        onClick = onFavoriteClicked
      ) {
        Icon(
          imageVector = if (isInFavorites) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
          contentDescription = "add to favorite",
          tint = Color.White
        )
      }

      IconButton(
        modifier = Modifier.padding(start = 16.dp),
        onClick = onSearchClickedClick
      ) {
        Icon(
          imageVector = if (isSearching) Icons.Default.Close else Icons.Default.Search,
          contentDescription = null,
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
fun SurasListPreview() {
  QuranyTheme {
    SurasScreenContent(
      reciterName = ReciterModel.mockList()[0].name
    )
  }
}