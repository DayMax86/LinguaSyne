package com.example.linguasyne.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable


private val LightThemeColors = lightColors(
    primary = LsTextBlue,
    primaryVariant = LsDarkPurple,
    onPrimary = LsGrey,
    secondary = LsDarkPurple,
    secondaryVariant = LsLightTeal,
    onSecondary = LsGrey,
    error = LsErrorRed,
    onError = LsErrorRed,
    background = LsBackgroundTeal,
    onBackground = White,
    surface = LsBackgroundTeal,
    onSurface = White
)

private val DarkThemeColors = lightColors(
    primary = LsMidPurple,
    primaryVariant = LsDarkPurple,
    onPrimary = LsGrey,
    secondary = LsDarkPurple,
    secondaryVariant = LsLightTeal,
    onSecondary = LsGrey,
    error = LsErrorRed,
    onError = LsErrorRed,
    background = LsBackgroundTeal,
    onBackground = White,
    surface = LsBackgroundTeal,
    onSurface = White
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

