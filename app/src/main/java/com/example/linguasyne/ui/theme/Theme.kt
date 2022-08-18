package com.example.linguasyne.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape


private val LightThemeColors = lightColors(
    primary = LsTextBlue,
    primaryVariant = LsPurple500,
    onPrimary = Black,
    secondary = LsVocabTextBlue,
    secondaryVariant = LsTeal200,
    onSecondary = Black,
    error = LsErrorRed,
    onError = LsErrorRed,
    background = LsBackgroundTeal,
    onBackground = Black,
    surface = LsBackgroundTeal,
    onSurface = Black
)

private val DarkThemeColors = lightColors(
    primary = LsPurple200,
    primaryVariant = LsPurple500,
    onPrimary = Black,
    secondary = LsVocabTextBlue,
    secondaryVariant = LsTeal200,
    onSecondary = Black,
    error = LsErrorRed,
    onError = LsErrorRed,
    background = LsBackgroundTeal,
    onBackground = Black,
    surface = LsBackgroundTeal,
    onSurface = Black
)


@Composable
fun LinguaSyneTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = if (darkTheme) DarkThemeColors else LightThemeColors,
        typography = Typography,
        content = content,
    )
}

