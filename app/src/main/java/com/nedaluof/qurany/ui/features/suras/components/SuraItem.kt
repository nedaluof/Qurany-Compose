package com.nedaluof.qurany.ui.features.suras.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nedaluof.data.model.SuraModel
import com.nedaluof.qurany.R
import com.nedaluof.qurany.ui.theme.AppGreen
import com.nedaluof.qurany.ui.theme.QuranyTheme

/**
 * Created By NedaluOf - 6/4/2024.
 */
@Composable
fun SuraItem(
  sura: SuraModel,
  onPlayClicked: () -> Unit = {},
  onDownloadClicked: () -> Unit = {}
) {
  Card(
    onClick = onPlayClicked,
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
    shape = RoundedCornerShape(8.dp),
    border = BorderStroke(1.dp, AppGreen),
    modifier = Modifier
      .padding(horizontal = 8.dp)
      .height(90.dp)
      .padding(bottom = 8.dp)
  ) {
    Row(Modifier.fillMaxWidth()) {
      Image(
        painter = painterResource(id = R.drawable.ic_opened_quran),
        contentDescription = "opened quran",
        modifier = Modifier
          .width(70.dp)
          .padding(start = 8.dp, end = 8.dp)
          .align(Alignment.CenterVertically)
      )
      Spacer(
        modifier = Modifier
          .padding(horizontal = 4.dp)
          .width(2.dp)
      )
      Column(
        Modifier
          .weight(1f)
          .align(Alignment.CenterVertically)
      ) {
        Text(
          text = sura.name,
          maxLines = 1,
          style = MaterialTheme.typography.titleMedium,
          color = AppGreen
        )

        Text(
          text = "${stringResource(id = R.string.rewaya_label)} ${sura.rewaya}",
          style = MaterialTheme.typography.bodySmall,
          color = AppGreen
        )

        Text(
          text = stringResource(id = R.string.sura_number_label, sura.id),
          style = MaterialTheme.typography.bodySmall,
          color = AppGreen
        )
      }

      Column(
        Modifier
          .background(MaterialTheme.colorScheme.primary)
          .fillMaxHeight()
      ) {
        IconButton(
          modifier = Modifier
            .weight(1f)
            .padding(6.dp), onClick = onPlayClicked
        ) {
          Icon(
            painter = painterResource(id = R.drawable.ic_play),
            contentDescription = "run sura in media player",
            tint = Color.White
          )
        }

        IconButton(
          modifier = Modifier
            .padding(6.dp)
            .weight(1f),
          onClick = onDownloadClicked,
        ) {
          Icon(
            painter = painterResource(id = R.drawable.ic_download),
            contentDescription = "download sura to your local storage",
            tint = Color.White
          )
        }
      }
    }
  }
}

@Preview
@Composable
fun SuraItemPreview(modifier: Modifier = Modifier) {
  QuranyTheme {
    SuraItem(sura = SuraModel().apply {
      id = 1
      name = "Al Fatiha"
      rewaya = "Hafs An Assem"
      reciterName = "Maher Al Mueqle"
      playerTitle = "Al Fatiha | Maher Al Mueqle"
    })
  }
}