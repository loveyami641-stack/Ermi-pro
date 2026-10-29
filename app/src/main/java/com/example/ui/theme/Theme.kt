package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ErmiColorScheme = darkColorScheme(
    primary = ErmiPurpleLight,
    onPrimary = StudioWhite,
    primaryContainer = ErmiPurpleContainer,
    onPrimaryContainer = ErmiPurpleLight,
    secondary = ErmiOrange,
    onSecondary = Color.Black,
    secondaryContainer = ErmiOrangeContainer,
    onSecondaryContainer = ErmiOrangeLight,
    tertiary = StudioCyan,
    onTertiary = Color.Black,
    background = StudioBackground,
    onBackground = StudioWhite,
    surface = StudioSurface,
    onSurface = StudioWhite,
    surfaceVariant = StudioSurfaceVariant,
    onSurfaceVariant = StudioTextSecondary,
    outline = StudioBorder,
    outlineVariant = StudioBorder.copy(alpha = 0.5f),
    error = StatusCancelled,
    onError = StudioWhite
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Ermi Pro uses a dedicated cinematic studio dark theme matching the brand logo
    MaterialTheme(
        colorScheme = ErmiColorScheme,
        typography = Typography,
        content = content
    )
}
