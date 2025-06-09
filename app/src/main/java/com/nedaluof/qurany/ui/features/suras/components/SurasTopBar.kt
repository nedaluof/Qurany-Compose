package com.nedaluof.qurany.ui.features.suras.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nedaluof.qurany.R

/**
 * Created By NedaluOf - 09/06/2025.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SurasTopBar(
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
    modifier = modifier.clip(RoundedCornerShape(bottomEnd = 20.dp)),
    scrollBehavior = scrollBehavior,
    //windowInsets = WindowInsets(top = 200),
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
        modifier = Modifier,
        onClick = onFavoriteClicked
      ) {
        Icon(
          imageVector = if (isInFavorites) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
          contentDescription = "add to favorite",
          tint = Color.White
        )
      }

      IconButton(
        modifier = Modifier.padding(horizontal = 8.dp),
        onClick = onSearchClickedClick
      ) {
        Icon(
          imageVector = if (isSearching) Icons.Default.Close else Icons.Default.Search,
          contentDescription = null,
          tint = Color.White
        )
      }
    }
  )
}