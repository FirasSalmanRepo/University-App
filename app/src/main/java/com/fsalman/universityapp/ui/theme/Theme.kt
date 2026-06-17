package com.fsalman.universityapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val UniversityColorScheme = lightColorScheme(
    primary = DeepNavy,
    onPrimary = TextOnNavy,
    primaryContainer = DeepNavyLight,
    onPrimaryContainer = TextOnNavy,
    secondary = Gold,
    onSecondary = TextOnGold,
    secondaryContainer = Gold,
    onSecondaryContainer = TextOnGold,
    tertiary = Gold,
    onTertiary = TextOnGold,
    background = Background,
    onBackground = TextPrimary,
    surface = Background,
    onSurface = TextPrimary,
    surfaceVariant = ContactCard,
    onSurfaceVariant = TextSecondary,
    outline = DividerColor,
    outlineVariant = DividerColor
)

@Composable
fun UniversityAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = UniversityColorScheme,
        typography = Typography,
        content = content
    )
}
