package com.example.linguasyne.ui.animations

import androidx.compose.animation.*
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
fun AnimateSuccess(
    animate: Boolean,
    animationSpec: FiniteAnimationSpec<Float>,
    initialScale: Float,
    transformOrigin: TransformOrigin,
) {

    Column(
        modifier = Modifier
            .fillMaxHeight(0.5f),
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        )
        {

            AnimatedVisibility(
                modifier = Modifier
                    .padding(2.dp),
                visible = animate,

                enter = scaleIn(
                    animationSpec = animationSpec,
                    initialScale = initialScale,
                    transformOrigin = transformOrigin,
                ) + expandVertically(
                    expandFrom = Alignment.CenterVertically
                ) + expandHorizontally(
                    expandFrom = Alignment.CenterHorizontally
                ),

                exit = scaleOut() + shrinkVertically(shrinkTowards = Alignment.CenterVertically)
            ) {
                Row(
                    Modifier
                        .size(200.dp)
                        .border(4.dp, MaterialTheme.colors.secondary, shape = CircleShape)
                        .background(
                            color = LsCorrectGreen, shape = CircleShape
                        ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row {
                        Text(
                            text = String(Character.toChars(0x2713)),
                            //color = LsCorrectGreen,
                            style =
                            TextStyle(
                                color = White,
                                fontSize = 150.sp,
                            ),

                            )
                    }
                }
            }
        }
    }
}