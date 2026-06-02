package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = TelegramDarkAccent,
    secondary = TelegramDarkSentBubble,
    tertiary = TelegramSkyBlue,
    background = TelegramDarkBg,
    surface = TelegramDarkHeader,
    onPrimary = Color.White,
    onBackground = TelegramDarkText,
    onSurface = TelegramDarkText,
    primaryContainer = TelegramDarkHeader,
    surfaceVariant = TelegramDarkReceivedBubble
)

private val LightColorScheme = lightColorScheme(
    primary = TelegramBlue,
    secondary = TelegramSkyBlue,
    tertiary = TelegramSkyBlue,
    background = TelegramLightBg,
    surface = TelegramLightCard,
    onPrimary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    primaryContainer = TelegramBlue,
    surfaceVariant = TelegramLightReceivedBubble
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
