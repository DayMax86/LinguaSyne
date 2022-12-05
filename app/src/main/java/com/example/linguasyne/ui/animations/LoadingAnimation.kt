package com.example.linguasyne.ui.animations

import androidx.compose.animation.*
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.linguasyne.ui.theme.LsCorrectGreen
import com.example.linguasyne.ui.theme.White

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