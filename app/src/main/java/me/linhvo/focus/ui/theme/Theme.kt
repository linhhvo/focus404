package me.linhvo.focus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    background = Ink,
    onBackground = Cream,
    inverseSurface = Cream,
    inverseOnSurface = Ink,
    surfaceContainerHighest = Charcoal,
    onSurface = Cream,
    primary = Sage,
    onPrimary = Forest,
    secondary = Bubblegum,
    onSecondary = Burgundy,
//    onSecondary = Ink,
    tertiary = Butter,
    onTertiary = Tangerine,
    primaryContainer = Burgundy,
    onPrimaryContainer = HotPink,
    secondaryContainer = BabyBlue,
    onSecondaryContainer = Banana,
    tertiaryContainer = Coral,
    onTertiaryContainer = Ink,
    primaryFixed = Lavender,
    onPrimaryFixed = Grape,
    surfaceVariant = Granite

)

@Composable
fun FocusTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}