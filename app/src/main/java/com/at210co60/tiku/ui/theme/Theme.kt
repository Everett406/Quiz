package com.at210co60.tiku.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.at210co60.tiku.data.repository.FontSize

private val WarmLightColorScheme = lightColorScheme(
    primary = AccentPrimary,
    onPrimary = Color.White,
    primaryContainer = AccentSecondary,
    onPrimaryContainer = TextPrimary,
    secondary = AccentSuccess,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD4E8DC),
    onSecondaryContainer = TextPrimary,
    tertiary = AccentInfo,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD4E4E6),
    onTertiaryContainer = TextPrimary,
    error = AccentError,
    onError = Color.White,
    errorContainer = Color(0xFFF5E0DE),
    onErrorContainer = TextPrimary,
    background = WarmWhite,
    onBackground = TextPrimary,
    surface = SurfaceColor,
    onSurface = TextPrimary,
    surfaceVariant = WarmCream,
    onSurfaceVariant = TextSecondary,
    outline = Border,
    outlineVariant = Color(0xFFD8D4CE),
    scrim = Color(0x52000000),
)

private val WarmDarkColorScheme = darkColorScheme(
    primary = DarkAccentPrimary,
    onPrimary = DarkBackground,
    primaryContainer = Color(0xFF5C4A2A),
    onPrimaryContainer = DarkTextPrimary,
    secondary = Color(0xFF9BBFA1),
    onSecondary = Color(0xFF1A3A1F),
    secondaryContainer = Color(0xFF2A5030),
    onSecondaryContainer = DarkTextPrimary,
    tertiary = Color(0xFFA0C4C7),
    onTertiary = Color(0xFF1A3537),
    tertiaryContainer = Color(0xFF2F5053),
    onTertiaryContainer = DarkTextPrimary,
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C2F2A),
    onErrorContainer = Color(0xFFF2B8B5),
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = Color(0xFF333333),
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorder,
    outlineVariant = Color(0xFF4A4A4A),
    scrim = Color(0x52000000),
)

private fun createTypographyScale(scale: Float = 1f) = Typography(
    displayLarge = TextStyle(fontSize = (34 * scale).sp, lineHeight = (40 * scale).sp, fontWeight = FontWeight.Bold),
    displayMedium = TextStyle(fontSize = (28 * scale).sp, lineHeight = (36 * scale).sp, fontWeight = FontWeight.Bold),
    headlineLarge = TextStyle(fontSize = (24 * scale).sp, lineHeight = (32 * scale).sp, fontWeight = FontWeight.SemiBold),
    headlineMedium = TextStyle(fontSize = (20 * scale).sp, lineHeight = (28 * scale).sp, fontWeight = FontWeight.SemiBold),
    titleLarge = TextStyle(fontSize = (18 * scale).sp, lineHeight = (26 * scale).sp, fontWeight = FontWeight.Medium),
    titleMedium = TextStyle(fontSize = (16 * scale).sp, lineHeight = (24 * scale).sp, fontWeight = FontWeight.Medium),
    bodyLarge = TextStyle(fontSize = (16 * scale).sp, lineHeight = (26 * scale).sp, fontWeight = FontWeight.Normal),
    bodyMedium = TextStyle(fontSize = (14 * scale).sp, lineHeight = (22 * scale).sp, fontWeight = FontWeight.Normal),
    bodySmall = TextStyle(fontSize = (13 * scale).sp, lineHeight = (18 * scale).sp, fontWeight = FontWeight.Normal),
    labelLarge = TextStyle(fontSize = (14 * scale).sp, lineHeight = (20 * scale).sp, fontWeight = FontWeight.Medium),
    labelMedium = TextStyle(fontSize = (12 * scale).sp, lineHeight = (16 * scale).sp, fontWeight = FontWeight.Medium),
    labelSmall = TextStyle(fontSize = (11 * scale).sp, lineHeight = (14 * scale).sp, fontWeight = FontWeight.Medium),
)

private val LocalFontSize = staticCompositionLocalOf { FontSize.NORMAL }

@Composable
fun TikuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    fontSize: FontSize = FontSize.NORMAL,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) WarmDarkColorScheme else WarmLightColorScheme

    val scale = when (fontSize) {
        FontSize.SMALL -> 0.875f
        FontSize.NORMAL -> 1f
        FontSize.LARGE -> 1.125f
        FontSize.EXTRA_LARGE -> 1.25f
    }

    val typography = createTypographyScale(scale)

    CompositionLocalProvider(LocalFontSize provides fontSize) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content,
        )
    }
}
