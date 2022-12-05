package com.example.linguasyne.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.linguasyne.ui.theme.LsGrey

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
                            LsGrey,
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
                            LsGrey,
                        )
                    )
                ),
        )
        {
            //
        }
    }
}
