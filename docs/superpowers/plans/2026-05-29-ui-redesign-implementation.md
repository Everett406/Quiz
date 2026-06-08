# Tiku UI Redesign - Warm Minimalism Implementation Plan

> **For agentic workers:** Execute tasks in order. Build and commit after each task group. Push and verify build after Task 7.

**Goal:** Transform Tiku into a warm, minimalist app with generous whitespace, soft colors, and humanistic feel.

**Architecture:** Design Tokens + M3E Override — define warm color/typography tokens, override TikuTheme, create warm-styled components, migrate screens incrementally.

**Tech Stack:** Kotlin, Jetpack Compose, Material 3 Expressive (overridden)

---

## File Change Map

| File | Action | Reason |
|------|--------|--------|
| `ui/theme/Color.kt` | Rewrite | Replace M3E colors with warm palette |
| `ui/theme/Type.kt` | Rewrite | Custom typography scale per spec |
| `ui/theme/Spacing.kt` | Create | Spacing constants |
| `ui/theme/Theme.kt` | Rewrite | Warm color schemes, disable dynamic color |
| `ui/components/WarmComponents.kt` | Create | Reusable warm-styled components |
| `ui/screen/home/HomeScreen.kt` | Rewrite | Warm minimalist layout |
| `ui/screen/quiz/QuizPracticeScreen.kt` | Rewrite | Warm option cards, clean layout |
| `ui/screen/wrong/WrongQuestionsScreen.kt` | Rewrite | Warm cards with subtle indicators |
| `ui/screen/settings/SettingsScreen.kt` | Rewrite | Clean list style |
| `ui/screen/detail/QuizDetailScreen.kt` | Rewrite | Warm action cards |
| `app/build.gradle.kts` | Modify | Version to v1.3.0 |
| `README.md` | Modify | Update changelog |

---

## Task 1: Rewrite Color.kt with Warm Palette

**Files:**
- Modify: `ui/theme/Color.kt`

- [ ] **Step 1: Rewrite Color.kt**

```kotlin
package com.at210co60.tiku.ui.theme

import androidx.compose.ui.graphics.Color

// ============ Warm Background Colors ============
val WarmWhite = Color(0xFFFAFAF8)          // Primary background
val WarmCream = Color(0xFFF5F3EF)         // Card backgrounds
val WarmGray = Color(0xFFEFECEA)          // Section dividers
val Surface = Color(0xFFFFFFFF)            // Elevated surfaces

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
val SurfaceLight = Surface
val SurfaceDark = DarkSurface
val SurfaceVariantLight = WarmCream
val SurfaceVariantDark = Color(0xFF333333)
val BackgroundLight = WarmWhite
val BackgroundDark = DarkBackground
val OutlineLight = Border
val OutlineDark = DarkBorder
val ErrorLight = AccentError
val ErrorDark = Color(0xFFF2B8B5)
```

---

## Task 2: Rewrite Type.kt with Clean Typography

**Files:**
- Modify: `ui/theme/Type.kt`

- [ ] **Step 1: Rewrite Type.kt**

```kotlin
package com.at210co60.tiku.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    // Display - Page titles
    displayLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        lineHeight = 40.sp,
    ),
    displayMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
    ),
    // Headline - Section headings
    headlineLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
    ),
    // Title - Card titles, buttons
    titleLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 26.sp,
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    // Body - Main content
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 26.sp,
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22.sp,
    ),
    bodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
    ),
    // Label - Badges, tags
    labelLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 14.sp,
    ),
)
```

---

## Task 3: Create Spacing.kt with Spacing Constants

**Files:**
- Create: `ui/theme/Spacing.kt`

- [ ] **Step 1: Create Spacing.kt**

```kotlin
package com.at210co60.tiku.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object Spacing {
    val xs: Dp = 4.dp
    val sm: Dp = 8.dp
    val md: Dp = 16.dp
    val lg: Dp = 24.dp
    val xl: Dp = 32.dp
    val xxl: Dp = 48.dp
}

object Radius {
    val sm: Dp = 8.dp
    val md: Dp = 12.dp
    val lg: Dp = 16.dp
    val xl: Dp = 24.dp
    val full: Dp = 9999.dp
}
```

---

## Task 4: Rewrite Theme.kt with Warm Color Schemes

**Files:**
- Modify: `ui/theme/Theme.kt`

- [ ] **Step 1: Rewrite Theme.kt**

```kotlin
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
    surface = Surface,
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

// Font size scales (same logic as before, using warm colors)
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
```

---

## Task 5: Create Warm Components

**Files:**
- Create: `ui/components/WarmComponents.kt`

- [ ] **Step 1: Create WarmComponents.kt**

```kotlin
package com.at210co60.tiku.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.at210co60.tiku.ui.theme.AccentError
import com.at210co60.tiku.ui.theme.AccentPrimary
import com.at210co60.tiku.ui.theme.AccentSuccess
import com.at210co60.tiku.ui.theme.Border
import com.at210co60.tiku.ui.theme.Radius
import com.at210co60.tiku.ui.theme.Spacing

@Composable
fun WarmButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        enabled = enabled,
        shape = RoundedCornerShape(Radius.full),
        colors = ButtonDefaults.buttonColors(
            containerColor = AccentPrimary,
            contentColor = Color.White,
            disabledContainerColor = Color(0xFFE8DCC8),
            disabledContentColor = Color(0xFF9A9A9A),
        ),
        contentPadding = PaddingValues(horizontal = Spacing.lg),
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(Spacing.sm))
        }
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun WarmOutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        enabled = enabled,
        shape = RoundedCornerShape(Radius.full),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = AccentPrimary,
        ),
        border = BorderStroke(1.dp, if (enabled) AccentPrimary else Border),
        contentPadding = PaddingValues(horizontal = Spacing.lg),
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun WarmCard(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(Spacing.lg),
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(Radius.lg),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, Border),
    ) {
        androidx.compose.foundation.layout.PaddingValues(
            start = contentPadding.calculateLeftPadding(androidx.compose.ui.unit.LayoutDirection.Ltr),
            top = contentPadding.calculateTopPadding(),
            end = contentPadding.calculateRightPadding(androidx.compose.ui.unit.LayoutDirection.Ltr),
            bottom = contentPadding.calculateBottomPadding(),
        ).let { pad ->
            androidx.compose.foundation.layout.Box(Modifier.padding(pad)) {
                content()
            }
        }
    }
}

@Composable
fun WarmProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    LinearProgressIndicator(
        progress = { progress },
        modifier = modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp)),
        color = AccentPrimary,
        trackColor = Border,
    )
}

@Composable
fun SuccessText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = AccentSuccess,
        modifier = modifier,
    )
}

@Composable
fun ErrorText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = AccentError,
        modifier = modifier,
    )
}
```

Note: For OptionCard, we'll define it inline in QuizPracticeScreen for now.

---

## Task 6: Rewrite HomeScreen with Warm Design

**Files:**
- Modify: `ui/screen/home/HomeScreen.kt`

- [ ] **Step 1: Rewrite HomeScreen with warm minimalist design**

Replace the current HomeScreen content with:
- Centered title "Tiku" and subtitle
- Single large warm card for default question bank
- Pill-shaped "加载示例" button
- Generous spacing (24px+ margins)
- Clean typography hierarchy

Key changes:
- Remove colored bank cards, use single warm cream card
- Use WarmButton and WarmCard components
- Increase padding and spacing
- Clean, minimal navigation

---

## Task 7: Rewrite QuizPracticeScreen with Warm Option Cards

**Files:**
- Modify: `ui/screen/quiz/QuizPracticeScreen.kt`

- [ ] **Step 1: Rewrite QuizPracticeScreen with warm design**

Key changes:
- Clean top bar with minimal back button
- Centered question counter
- Thin warm progress bar
- Warm cream option cards with subtle borders
- Selected state: amber border + checkmark
- Correct state: green background + border
- Wrong state: terracotta background + border
- WarmButton for confirm/next actions

---

## Task 8: Rewrite WrongQuestionsScreen

**Files:**
- Modify: `ui/screen/wrong/WrongQuestionsScreen.kt`

- [ ] **Step 1: Rewrite with warm minimalist design**

Key changes:
- Centered title "错题本" with subtitle showing count
- WarmCard for each wrong question
- Red dot indicator instead of heavy colors
- Show user answer and correct answer in contrasting colors
- Clean delete icon (close icon)

---

## Task 9: Rewrite SettingsScreen

**Files:**
- Modify: `ui/screen/settings/SettingsScreen.kt`

- [ ] **Step 1: Rewrite with warm design**

Key changes:
- Section headers in uppercase, muted color
- Simple list-style settings items
- Warm pill buttons for theme/font size selection
- Clear data in destructive style (subtle red)
- App info at bottom, centered and muted

---

## Task 10: Rewrite QuizDetailScreen

**Files:**
- Modify: `ui/screen/detail/QuizDetailScreen.kt`

- [ ] **Step 1: Rewrite with warm design**

Key changes:
- Warm cream background for action cards
- Clean typography for stats
- WarmButton for primary actions

---

## Task 11: Version Bump & README

**Files:**
- Modify: `app/build.gradle.kts`
- Modify: `README.md`

- [ ] **Step 1: Update version to v1.3.0**

```kotlin
versionCode = 7
versionName = "1.3.0"
```

- [ ] **Step 2: Update README changelog**

Add:
```markdown
| v1.3.0 | 2026-05-29 | UI 全面重构：暖色极简风格，大量留白，iOS 设计语言，温馨人文关怀 |
```

---

## Task 12: Build & Verify

- [ ] **Step 1: Push to GitHub**

```bash
git add -A && git commit -m "refactor: complete UI redesign with warm minimalist style"
git push origin main
```

- [ ] **Step 2: Wait for GitHub Actions**

Check workflow runs at: https://github.com/Everett406/Quiz/actions

- [ ] **Step 3: Verify build success**

Confirm workflow #N shows "Success" and Release v1.3.0 is created.
