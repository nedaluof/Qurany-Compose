package com.nedaluof.qurany.ui.screens.reciters

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nedaluof.data.model.ReciterModel
import com.nedaluof.qurany.R
import com.nedaluof.qurany.ui.theme.AppGreen

/**
 * Created By NedaluOf - 6/1/2024.
 */

@Composable
fun ReciterItem(
  reciter: ReciterModel,
  onClicked: () -> Unit = {},
  onAddToFavoriteClicked: () -> Unit = {},
) {
  Card(
    onClick = onClicked,
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
    shape = RoundedCornerShape(8.dp),
    border = BorderStroke(1.dp, AppGreen),
    modifier = Modifier
      .height(70.dp)
      .padding(horizontal = 8.dp)
      .padding(bottom = 8.dp)
  ) {
    Row(Modifier.fillMaxWidth()) {
      Image(
        painter = painterResource(id = R.drawable.ic_reciter),
        contentDescription = "reciter",
        Modifier
          .width(50.dp)
          .background(AppGreen)
          .padding(top = 6.dp)
      )
      Spacer(
        modifier = Modifier
          .padding(horizontal = 4.dp)
          .width(4.dp)
      )
      Column(
        Modifier.weight(1f)
      ) {
        Text(
          text = reciter.name,
          maxLines = 1,
          style = MaterialTheme.typography.titleMedium,
          color = AppGreen
        )

        Text(
          text = "${stringResource(id = R.string.rewaya_label)} ${reciter.rewaya}",
          style = MaterialTheme.typography.bodySmall,
          color = AppGreen
        )

        Text(
          text = if (reciter.count.toInt() > 1) {
            stringResource(id = R.string.sura_count_more_label, reciter.count.toInt())
          } else {
            stringResource(id = R.string.sura_count_one_label, reciter.count.toInt())
          }, style = MaterialTheme.typography.bodySmall, color = AppGreen
        )
      }

      IconButton(
        onClick = onAddToFavoriteClicked,
        modifier = Modifier.align(Alignment.CenterVertically)
      ) {
        Icon(
          if (reciter.isInMyFavorites) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
          contentDescription = "add to favorite",
          tint = AppGreen
        )
      }
    }
  }
}

@Preview
@Composable
fun ReciterItemPreview(modifier: Modifier = Modifier) {
  ReciterItem(reciter = ReciterModel.mockList()[0])
}