package com.at210co60.tiku.ui.theme

import androidx.compose.ui.graphics.Color

// ============ Warm Background Colors ============
val WarmWhite = Color(0xFFFAFAF8)          // Primary background
val WarmCream = Color(0xFFF5F3EF)         // Card backgrounds
val WarmGray = Color(0xFFEFECEA)          // Section dividers
val SurfaceColor = Color(0xFFFFFFFF)            // Elevated surfaces

// ============ Warm Text Colors ============
val TextPrimary = Color(0xFF1A1A1A)       // Headings, primary content
val TextSecondary = Color(0xFF5C5C5C)     // Secondary content
val TextTertiary = Color(0xFF9A9A9A)      // Placeholders, timestamps

// ============ Warm Accent Colors ============
val AccentPrimary = Color(0xFFC4A574)     // Primary actions (warm amber)
val AccentSecondary = Color(0xFFE8DCC8)    // Soft accent backgrounds
val AccentSuccess = Color(0xFF7BA889)      // Correct answers, success
val AccentError = Color(0xFFD4847C)        // Wrong answers, errors
val AccentInfo = Color(0xFF8BAAAD)        // Info states, badges

// ============ Dark Theme Colors ============
val DarkBackground = Color(0xFF1A1A1A)
val DarkSurface = Color(0xFF252525)
val DarkTextPrimary = Color(0xFFFAFAF8)
val DarkTextSecondary = Color(0xFFA0A0A0)
val DarkAccentPrimary = Color(0xFFD4B584)
val DarkBorder = Color(0xFF3A3A3A)

// ============ Border & Shadow ============
val Border = Color(0xFFE8E4DE)
val Shadow = Color(0x1A1A1A1A)

// ============ Legacy M3E aliases (for compatibility) ============
val PrimaryLight = AccentPrimary
val PrimaryDark = DarkAccentPrimary
val OnPrimaryLight = Color.White
val OnPrimaryDark = Color(0xFF1A1A1A)
val CardBlue = AccentSecondary
val CardGreen = Color(0xFFD4E8DC)
val CardYellow = AccentSecondary
val CardPink = Color(0xFFF5E0DE)
val SurfaceLight = SurfaceColor
val SurfaceDark = DarkSurface
val SurfaceVariantLight = WarmCream
val SurfaceVariantDark = Color(0xFF333333)
val BackgroundLight = WarmWhite
val BackgroundDark = DarkBackground
val OutlineLight = Border
val OutlineDark = DarkBorder
val ErrorLight = AccentError
val ErrorDark = Color(0xFFF2B8B5)
