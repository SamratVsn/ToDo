package com.example.todovsn.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DeepSeaColorScheme = darkColorScheme(
    primary = DeepSeaPrimary,
    onPrimary = DeepSeaBackground,
    surface = DeepSeaBackground,
    onSurface = DeepSeaOnSurface,
    background = DeepSeaBackground,
    onBackground = DeepSeaOnSurface,
    surfaceVariant = DeepSeaSurfaceVariant,
    onSurfaceVariant = DeepSeaOnSurfaceVariant,
    outline = DeepSeaOutline,
    primaryContainer = DeepSeaPrimary.copy(alpha = 0.15f),
    onPrimaryContainer = DeepSeaPrimary,
    secondaryContainer = DeepSeaSurfaceVariant,
    onSecondaryContainer = DeepSeaOnSurface,
    tertiaryContainer = DeepSeaOutline.copy(alpha = 0.3f),
    onTertiaryContainer = DeepSeaOnSurfaceVariant
)

private val LightColorScheme = lightColorScheme(
    primary = DeepSeaPrimary,
    onPrimary = Color.White,
    surface = Color(0xFFF8F9FF), // Very light blue tint
    onSurface = DeepSeaBackground,
    background = Color(0xFFF8F9FF),
    onBackground = DeepSeaBackground,
    surfaceVariant = Color.White,
    onSurfaceVariant = DeepSeaBackground.copy(alpha = 0.7f),
    outline = DeepSeaOutline, // Stronger outline for light mode
    primaryContainer = DeepSeaPrimary.copy(alpha = 0.12f),
    onPrimaryContainer = DeepSeaPrimary,
    secondaryContainer = DeepSeaBackground.copy(alpha = 0.05f),
    onSecondaryContainer = DeepSeaBackground,
    tertiaryContainer = DeepSeaOutline.copy(alpha = 0.1f),
    onTertiaryContainer = DeepSeaBackground
)

@Composable
fun TasksTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled for specific brand theme consistency
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DeepSeaColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
