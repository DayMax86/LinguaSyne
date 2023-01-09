package com.daymax86.linguasyne.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.daymax86.linguasyne.ui.theme.LsLightOnPrimary

@Composable
fun TopFadedBox(
    show: Boolean,
) {
    if (show) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            LsLightOnPrimary,
                            Color.Transparent,
                        )
                    )
                ),
        )
        {
            //
        }
    }
}

@Composable
fun BottomFadedBox(
    show: Boolean,
) {
    if (show) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            LsLightOnPrimary,
                        )
                    )
                ),
        )
        {
            //
        }
    }
}
