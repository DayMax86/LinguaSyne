package com.daymax86.linguasyne.ui.elements

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun SelectImage(
    userImage: Uri?,
    onImageSelected: (Uri) -> Unit,
) {

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            if (it != null) {
                onImageSelected(it)
            }
        }

    AsyncImage(
        modifier = Modifier
            //.padding(top = 30.dp)
            .clickable {
                launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            .border(
                color = MaterialTheme.colors.primary,
                width = 2.dp,
                shape = CircleShape
            )
            .size(width = 150.dp, height = 150.dp)
            .clip(shape = CircleShape),
        model = userImage,
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )

}