package com.daymax86.linguasyne.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.daymax86.linguasyne.R

val linguaSyneDefault = FontFamily(
    Font(R.font.cabin)
)

val Typography = Typography(
    h1 = TextStyle(
        fontFamily = linguaSyneDefault,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp
    ),

    body1 = TextStyle(
        fontFamily = linguaSyneDefault,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),

    body2 = TextStyle(
        fontFamily = linguaSyneDefault,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),

    button = TextStyle(
        fontFamily = linguaSyneDefault,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
    ),

    caption = TextStyle(
      fontFamily = linguaSyneDefault,
      fontWeight = FontWeight.Normal,
      fontSize = 14.sp,
        color = LsLightPrimaryVariant,
    )

)