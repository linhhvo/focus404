package me.linhvo.focus.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    background = Ink,
    onBackground = Cream,
    primary = Sage,
    secondary = Bubblegum,
    tertiary = Butter,
    onPrimary = Ink,
    onSecondary = Ink,
    onTertiary = Ink,
    secondaryContainer = Babyblue,
    tertiaryContainer = Coral
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