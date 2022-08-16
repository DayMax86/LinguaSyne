package com.example.linguasyne.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.linguasyne.R

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

    button = TextStyle(
        fontFamily = linguaSyneDefault,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,


    )

)