package com.nedaluof.qurany.ui.screens.main.reciters

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nedaluof.qurany.R
import com.nedaluof.qurany.data.model.Reciter
import com.nedaluof.qurany.ui.theme.AppGreen

/**
 * Created By NedaluOf - 6/1/2024.
 */

@Composable
fun ReciterItem(
  reciter: Reciter,
  onClicked: () -> Unit = {},
  onAddToFavoriteClicked: () -> Unit = {},
) {
  var isInMyReciters by remember { mutableStateOf(reciter.inMyReciters) }
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
          text = reciter.name ?: "",
          maxLines = 1,
          style = MaterialTheme.typography.titleMedium,
          color = AppGreen
        )

        Text(
          text = reciter.rewaya ?: "", style = MaterialTheme.typography.bodySmall, color = AppGreen
        )

        Text(
          text = reciter.count ?: "", style = MaterialTheme.typography.bodySmall, color = AppGreen
        )
      }

      IconButton(
        onClick = {
          isInMyReciters = !isInMyReciters
          onAddToFavoriteClicked()
        }, modifier = Modifier.align(Alignment.CenterVertically)
      ) {
        Icon(
          painter = painterResource(id = R.drawable.ic_favorite_navigation),
          contentDescription = "add to favorite",
          tint = if (isInMyReciters) Color.Red else AppGreen
        )
      }
    }
  }
}

@Preview
@Composable
fun ReciterItemPreview(modifier: Modifier = Modifier) {
  ReciterItem(reciter = Reciter().apply {
    id = "1"
    name = "Maher Al-Mueqle"
    rewaya = "Hafs An Asem"
    count = "There 114 Suras"
    letter = ""
    suras = ""
    inMyReciters = true
  })
}