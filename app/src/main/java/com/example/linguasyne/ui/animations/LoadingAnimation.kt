package com.example.linguasyne.ui.animations

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimateLoading(
    animate: Boolean,
    modifier: Modifier,
) {
    if (animate) {
        Box(
            modifier = modifier,
                contentAlignment = Alignment.Center,
        ) {

            CircularProgressIndicator(
                color = MaterialTheme.colors.primary
            )

        }
    }
}