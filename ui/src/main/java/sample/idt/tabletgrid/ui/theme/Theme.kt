package sample.idt.tabletgrid.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = LightThemePrimary,
    primaryContainer = LightThemePrimaryContainer,
    secondary = LightThemeSecondary,
    secondaryContainer = LightThemeSecondaryContainer,
    tertiary = LightThemeTertiary,
    tertiaryContainer = LightThemeTertiaryContainer,
    onTertiaryContainer = LightThemeOnTertiaryContainer,
    error = LightThemeError,
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkThemePrimary,
    primaryContainer = DarkThemePrimaryContainer,
    secondary = DarkThemeSecondary,
    secondaryContainer = DarkThemeSecondaryContainer,
    tertiary = DarkThemeTertiary,
    tertiaryContainer = DarkThemeTertiaryContainer,
    onTertiaryContainer = DarkThemeOnTertiaryContainer,
    error = DarkThemeError,
)

@Composable
fun TabletGridTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = TabletGridTypography,
        content = content,
    )
}
