package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryGreen,
    onPrimary = Color.White,
    primaryContainer = DarkGreen,
    onPrimaryContainer = QuranGoldLight,
    secondary = QuranGold,
    onSecondary = DeepEmerald,
    secondaryContainer = CardDark,
    onSecondaryContainer = QuranGoldLight,
    tertiary = QuranGoldLight,
    onTertiary = DeepEmerald,
    background = BackgroundDark,
    onBackground = TextPrimaryDark,
    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = CardDark,
    onSurfaceVariant = TextSecondaryDark,
    outline = DividerDark
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = Color.White,
    primaryContainer = QuranGoldLight,
    onPrimaryContainer = DarkGreen,
    secondary = QuranGold,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEDF5F1),
    onSecondaryContainer = DarkGreen,
    tertiary = QuranGoldDark,
    onTertiary = Color.White,
    background = BackgroundLight,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFF1EFE6),
    onSurfaceVariant = TextSecondaryLight,
    outline = DividerLight
)

@Composable
fun QuranKarimTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = QuranTypography,
        content = content
    )
}
