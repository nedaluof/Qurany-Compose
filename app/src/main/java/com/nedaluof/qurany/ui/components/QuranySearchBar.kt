package com.nedaluof.qurany.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nedaluof.qurany.R
import com.nedaluof.qurany.ui.theme.QuranyComposeTheme

/**
 * Created By NedaluOf - 6/15/2024.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranySearchBar(
  modifier: Modifier = Modifier, text: String, placeHolder: String, onTextChange: (String) -> Unit
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .border(
        BorderStroke(
          0.1.dp, SolidColor(MaterialTheme.colorScheme.primary)
        ), RoundedCornerShape(12.dp)
      ), contentAlignment = Alignment.Center
  ) {
    OutlinedTextField(
      modifier = Modifier.fillMaxWidth(),
      value = text,
      onValueChange = onTextChange,
      placeholder = {
        Text(
          text = placeHolder,
          color = MaterialTheme.colorScheme.primary,
          fontSize = 14.sp,
          fontWeight = FontWeight.SemiBold,
        )
      },
      leadingIcon = {
        IconButton(onClick = {}) {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp)
          )
        }
      },
      trailingIcon = {
        IconButton(onClick = { onTextChange("") }) {
          Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp)
          )
        }
      },
      keyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Search
      ),
      keyboardActions = KeyboardActions(onSearch = {
        onTextChange(text)
      }),
      singleLine = true,
      textStyle = TextStyle(
        color = MaterialTheme.colorScheme.primary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Normal,

        ),
      colors = TextFieldDefaults.outlinedTextFieldColors(
        containerColor = MaterialTheme.colorScheme.background,
        focusedBorderColor = Color.Transparent,
        unfocusedBorderColor = Color.Transparent,
        cursorColor = MaterialTheme.colorScheme.primary
      ),
    )
    SearchBarDivider(
      modifier = Modifier
        .align(Alignment.CenterEnd)
        .padding(horizontal = 50.dp)
    )
  }
}

@Composable
fun SearchBarDivider(
  modifier: Modifier = Modifier
) {
  HorizontalDivider(
    modifier = modifier.width(1.dp), color = MaterialTheme.colorScheme.primary, thickness = 20.dp
  )
}

@Composable
@Preview()
fun SearchPreview() {
  QuranyComposeTheme(true) {
    Box(
      modifier = Modifier
        .background(Color.White)
        .padding(40.dp)
        .fillMaxWidth()
        .height(100.dp),
      contentAlignment = Alignment.Center
    ) {
      QuranySearchBar(
        text = "",
        onTextChange = {},
        placeHolder = stringResource(id = R.string.reciters_search_hint_label)
      )
    }
  }
}