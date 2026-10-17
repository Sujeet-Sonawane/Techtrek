package com.nexus.app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val NexusDarkColorScheme = darkColorScheme(
    primary = NexusBluePrimary,
    onPrimary = NexusTextPrimary,
    primaryContainer = NexusBlueDark,
    onPrimaryContainer = NexusBlueGlow,
    secondary = NexusBlueLight,
    onSecondary = NexusTextPrimary,
    background = NexusDarkBg,
    onBackground = NexusTextPrimary,
    surface = NexusSurface,
    onSurface = NexusTextPrimary,
    surfaceVariant = NexusSurfaceVariant,
    onSurfaceVariant = NexusTextSecondary,
    outline = NexusBorder,
    error = NexusRed,
    onError = NexusTextPrimary
)

@Composable
fun NexusTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = NexusDarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = NexusDarkBg.toArgb()
                window.navigationBarColor = NexusDarkBg.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
