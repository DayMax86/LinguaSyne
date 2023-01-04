package com.example.linguasyne.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable


private val LightThemeColors = lightColors(
    primary = LsLightPrimary,
    primaryVariant = LsLightPrimaryVariant,
    onPrimary = LsLightOnPrimary,
    secondary = LsLightSecondary,
    secondaryVariant = LsLightSecondaryVariant,
    onSecondary = LsLightOnSecondary,
    error = LsErrorRed,
    onError = LsErrorRed,
    background = LsLightBackground,
    onBackground = LsLightOnBackground,
    surface = LsLightSurface,
    onSurface = LsLightOnSurface
)

private val DarkThemeColors = darkColors(
    primary = LsDarkPrimary,
    primaryVariant = LsDarkPrimaryVariant,
    onPrimary = LsDarkOnPrimary,
    secondary = LsDarkSecondary,
    secondaryVariant = LsDarkSecondaryVariant,
    onSecondary = LsDarkOnSecondary,
    error = LsErrorRed,
    onError = LsErrorRed,
    background = LsDarkBackground,
    onBackground = LsDarkOnBackground,
    surface = LsDarkSurface,
    onSurface = LsDarkOnSurface
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

