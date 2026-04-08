package com.nust.valentinegarage.core.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

import androidx.compose.ui.graphics.Color
import com.nust.valentinegarage.core.model.AppThemeMode

private val DarkColorScheme = darkColorScheme(
    primary = IndustrialOrange,
    secondary = SteelBlue,
    tertiary = WarningAmber,
    background = DeepSlate,
    surface = DeepSlate,
    surfaceVariant = Color(0xFF263238),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = LightAsh,
    onSurface = LightAsh,
    onSurfaceVariant = Color(0xFFB0BEC5),
    error = ErrorRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = IndustrialOrange,
    secondary = SteelBlue,
    tertiary = WarningAmber,
    background = LightAsh,
    surface = Color.White,
    surfaceVariant = SurfaceContainerLow,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = DeepSlate,
    onSurface = DeepSlate,
    onSurfaceVariant = Color.DarkGray,
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun ValentineGarageTheme(
    themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        AppThemeMode.LIGHT -> false
        AppThemeMode.DARK -> true
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
    }
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
