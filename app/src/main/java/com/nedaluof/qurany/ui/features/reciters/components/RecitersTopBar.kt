package com.nedaluof.qurany.ui.features.reciters.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nedaluof.qurany.R

/**
 * Created By NedaluOf - 09/06/2025.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecitersTopBar(
  isSearching: Boolean,
  onSearchClickedClick: () -> Unit,
  scrollBehavior: TopAppBarScrollBehavior
) {
  CenterAlignedTopAppBar(
    modifier = Modifier
      .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp)),
    scrollBehavior = scrollBehavior,
    windowInsets = WindowInsets(top = 40),
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
        modifier = Modifier.padding(end = 40.dp),
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