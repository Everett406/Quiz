# Tiku UI Redesign - Warm Minimalism

> **Design Direction:** Warm, minimalist iOS-inspired interface with generous whitespace, soft shadows, and a humanistic feel.

**Goal:** Transform Tiku into an app with a warm, inviting aesthetic that prioritizes clarity and calmness. Inspired by iOS native apps — ample whitespace, restrained color use, and gentle interactions.

**Design Philosophy:** "Less, but better." — Dieter Rams

---

## 1. Design Language

### Color Palette

#### Background Colors
| Token | Hex | Usage |
|-------|-----|-------|
| `background.primary` | `#FAFAF8` | Main background (warm white) |
| `background.secondary` | `#F5F3EF` | Card backgrounds (warm cream) |
| `background.tertiary` | `#EFECEA` | Subtle section dividers |

#### Text Colors
| Token | Hex | Usage |
|-------|-----|-------|
| `text.primary` | `#1A1A1A` | Headings, primary content |
| `text.secondary` | `#5C5C5C` | Secondary content, descriptions |
| `text.tertiary` | `#9A9A9A` | Placeholder text, timestamps |
| `text.inverse` | `#FFFFFF` | Text on colored backgrounds |

#### Accent Colors
| Token | Hex | Usage |
|-------|-----|-------|
| `accent.primary` | `#C4A574` | Primary actions (warm amber/gold) |
| `accent.secondary` | `#E8DCC8` | Soft accent backgrounds |
| `accent.success` | `#7BA889` | Correct answers, success states |
| `accent.error` | `#D4847C` | Wrong answers, error states |
| `accent.info` | `#8BAAAD` | Info states, badges |

#### Surface & Border
| Token | Hex | Usage |
|-------|-----|-------|
| `surface` | `#FFFFFF` | Cards, elevated surfaces |
| `border` | `#E8E4DE` | Subtle warm gray borders |
| `shadow` | `#1A1A1A10` | Soft shadows (10% opacity) |

### Typography

Use system font (SF Pro on iOS, Roboto on Android with similar weights).

| Style | Size | Weight | Line Height | Usage |
|-------|------|--------|-------------|-------|
| `display` | 34sp | Bold (700) | 1.2 | Page titles |
| `headline` | 24sp | SemiBold (600) | 1.3 | Section headings |
| `title` | 20sp | Medium (500) | 1.4 | Card titles, button text |
| `body` | 16sp | Regular (400) | 1.6 | Main content |
| `caption` | 13sp | Regular (400) | 1.4 | Timestamps, hints |
| `label` | 12sp | Medium (500) | 1.3 | Badges, tags |

### Spacing System

Base unit: 8px

| Token | Value | Usage |
|-------|-------|-------|
| `space.xs` | 4px | Tight spacing, icon padding |
| `space.sm` | 8px | Inline element gaps |
| `space.md` | 16px | Standard padding |
| `space.lg` | 24px | Section spacing |
| `space.xl` | 32px | Page margins |
| `space.2xl` | 48px | Major section breaks |

### Corner Radius

| Token | Value | Usage |
|-------|-------|-------|
| `radius.sm` | 8px | Small buttons, chips |
| `radius.md` | 12px | Cards, inputs |
| `radius.lg` | 16px | Modal sheets |
| `radius.xl` | 24px | Large cards |
| `radius.full` | 9999px | Pills, circular buttons |

### Shadows

| Token | Value | Usage |
|-------|-------|-------|
| `elevation.sm` | 0 1px 3px rgba(26,26,26,0.06) | Subtle lift |
| `elevation.md` | 0 4px 12px rgba(26,26,26,0.08) | Cards |
| `elevation.lg` | 0 8px 24px rgba(26,26,26,0.12) | Modals |

### Motion

- Duration: 200-300ms for micro-interactions, 400ms for page transitions
- Easing: `cubic-bezier(0.25, 0.1, 0.25, 1)` (ease-out)
- Prefer subtle fade + slight translate over bouncy animations

---

## 2. Component Redesign

### Button

| State | Background | Text | Border |
|-------|-----------|------|--------|
| Primary Default | `#C4A574` | `#FFFFFF` | none |
| Primary Pressed | `#B39665` | `#FFFFFF` | none |
| Primary Disabled | `#E8DCC8` | `#9A9A9A` | none |
| Secondary Default | transparent | `#C4A574` | 1px `#C4A574` |
| Secondary Pressed | `#F5F3EF` | `#B39665` | 1px `#B39665` |

Height: 48px, Padding: 16px horizontal, Radius: 24px (pill shape)

### Card

- Background: `#FFFFFF`
- Border: 1px `#E8E4DE`
- Border-radius: 16px
- Padding: 20px
- Shadow: `elevation.sm` (subtle)
- No heavy shadows — the warm background provides enough depth

### Option Card (for quiz answers)

| State | Background | Border | Icon |
|-------|-----------|--------|------|
| Default | `#FAFAF8` | 1px `#E8E4DE` | none |
| Selected | `#E8DCC8` | 2px `#C4A574` | checkmark |
| Correct | `#D4E8DC` | 2px `#7BA889` | checkmark |
| Wrong | `#F5E0DE` | 2px `#D4847C` | X mark |

### Input Field

- Background: `#F5F3EF`
- Border: none (borderless style)
- Border-radius: 12px
- Padding: 16px
- Focus state: subtle underline or glow in accent color

### Bottom Navigation / Tab Bar

Not used — use simple back buttons and minimal navigation.

### Progress Indicator

- Track: `#E8E4DE` (warm gray)
- Fill: `#C4A574` (warm amber)
- Height: 6px
- Border-radius: 3px (fully rounded)

---

## 3. Page Layouts

### Design Principles

1. **Generous margins:** 24px minimum horizontal padding on all screens
2. **Vertical rhythm:** 24px spacing between major sections
3. **Single column focus:** Content centered or left-aligned, max-width 600dp for readability
4. **Floating action:** Primary actions float at bottom with safe area padding
5. **Breathing room:** No cramped elements — every section has space to "breathe"

### Home Screen

```
┌────────────────────────────────────┐
│ [Safe area]                        │
│                                    │
│         Tiku                      │ ← Display title, centered, #1A1A1A
│       题库练习                      │ ← Subtitle, #5C5C5C
│                                    │
│ ┌────────────────────────────────┐ │
│ │                                │ │
│ │     📚 Kotlin 基础题库          │ │ ← Single large card
│ │     15 道题目                  │ │    Rounded 16px
│ │     ────────────               │ │    Warm cream background
│ │     ✓ 80% 正确率               │ │
│ │                                │ │
│ └────────────────────────────────┘ │
│                                    │
│         [ 加载示例 ]               │ ← Pill button, centered
│                                    │
│ [Safe area]                        │
└────────────────────────────────────┘
```

### Quiz Practice Screen

```
┌────────────────────────────────────┐
│ [Safe area]                        │
│                           [←]     │ ← Minimal back button
│                                    │
│         3 / 15                    │ ← Question counter, centered
│                                    │
│    ████████████░░░░░░░░░░░░       │ ← Thin progress bar
│                                    │
│         单选题                     │ ← Question type badge
│                                    │
│   Kotlin 中 val 和 var 的区别       │ ← Question text, large
│   是什么？                         │
│                                    │
│   ┌────────────────────────────┐  │
│   │  A. val 是可变的...          │  │ ← Option cards
│   └────────────────────────────┘  │
│   ┌────────────────────────────┐  │
│   │  B. val 是不可变的...        │  │
│   └────────────────────────────┘  │
│   ┌────────────────────────────┐  │
│   │  C. 两者没有区别             │  │
│   └────────────────────────────┘  │
│                                    │
│        [ 确认答案 ]                │ ← Primary action
│                                    │
│ [Safe area]                        │
└────────────────────────────────────┘
```

### Wrong Questions Screen

```
┌────────────────────────────────────┐
│ [Safe area]                        │
│                          [←]      │
│                                    │
│         错题本                     │ ← Centered title
│         共 5 道题                 │ ← Subtitle
│                                    │
│ ┌────────────────────────────────┐ │
│ │  ● val 和 var 的区别            │ │ ← Red dot indicator
│ │    单选 · 3小时前               │ │    Timestamp in tertiary color
│ │    ─────────────────────────   │ │
│ │    你的答案：B. val 是可变的     │ │ ← User's wrong answer
│ │    正确答案：A. val 是不可变的  │ │ ← Correct answer
│ └────────────────────────────────┘ │
│                                    │
│ [Safe area]                        │
└────────────────────────────────────┘
```

### Settings Screen

```
┌────────────────────────────────────┐
│ [Safe area]                        │
│                          [←]      │
│                                    │
│         设置                       │
│                                    │
│ ┌────────────────────────────────┐ │
│ │                                │ │
│ │  外观                          │ │ ← Section header
│ │                                │ │
│ │  主题          跟随系统       > │ │ ← Simple list items
│ │  ─────────────────────────────  │
│ │  字号          标准          > │ │
│ │                                │ │
│ └────────────────────────────────┘ │
│                                    │
│ ┌────────────────────────────────┐ │
│ │                                │ │
│ │  数据                          │ │
│ │                                │ │
│ │  清除所有数据               > │ │ ← With destructive style
│ │                                │ │
│ └────────────────────────────────┘ │
│                                    │
│         Tiku v1.2.0               │ ← App info, centered, subtle
│       一款面向学生的刷题应用        │
│                                    │
│ [Safe area]                        │
└────────────────────────────────────┘
```

---

## 4. Color Application in UI

### Theme Mode

| Element | Light Mode | Dark Mode |
|---------|-----------|-----------|
| Background | `#FAFAF8` | `#1A1A1A` |
| Surface | `#FFFFFF` | `#252525` |
| Text Primary | `#1A1A1A` | `#FAFAF8` |
| Text Secondary | `#5C5C5C` | `#A0A0A0` |
| Accent | `#C4A574` | `#D4B584` |
| Border | `#E8E4DE` | `#3A3A3A` |

### Color Usage by Context

- **Primary Actions:** Accent amber (`#C4A574`)
- **Success States:** Sage green (`#7BA889`)
- **Error States:** Muted terracotta (`#D4847C`)
- **Info/Badges:** Dusty blue (`#8BAAAD`)
- **Dividers:** Warm gray (`#E8E4DE`)
- **Disabled:** Light warm gray (`#E8E4DE`)

---

## 5. Implementation Approach

### Strategy: Design Tokens + M3E Override

1. **Define warm color tokens** in `Color.kt` (replacing M3E defaults)
2. **Create custom typography scale** in `Type.kt`
3. **Override M3E's `TikuTheme`** to use warm palette
4. **Create reusable components** with warm styling:
   - `WarmButton`
   - `WarmCard`
   - `WarmOptionCard`
   - `WarmTextField`
   - `WarmProgressBar`
5. **Update spacing constants** in a `Spacing.kt` file
6. **Gradually migrate screens** to use new components

### File Changes

| File | Changes |
|------|---------|
| `ui/theme/Color.kt` | Define warm palette, override M3E seeds |
| `ui/theme/Type.kt` | Custom typography scale |
| `ui/theme/Spacing.kt` | Create spacing constants |
| `ui/theme/Theme.kt` | Update TikuTheme with warm overrides |
| `ui/components/*` | Create warm-styled reusable components |
| `ui/screen/home/*` | Redesign with warm components |
| `ui/screen/quiz/*` | Redesign with warm components |
| `ui/screen/wrong/*` | Redesign with warm components |
| `ui/screen/settings/*` | Redesign with warm components |
| `ui/screen/detail/*` | Redesign with warm components |

### Backward Compatibility

- M3E's dynamic color (Material You) will be disabled or overridden
- Keep M3E's component behavior (gestures, accessibility) intact
- Only override visual appearance, not interaction patterns

---

## 6. Accessibility

- Maintain minimum touch target size: 44x44dp
- Color contrast ratio: 4.5:1 minimum for text
- Support system font scaling
- Respect reduced motion preferences
- Maintain screen reader compatibility

---

## 7. Key Differences from Current Design

| Aspect | Current (M3E) | New (Warm Minimal) |
|--------|---------------|-------------------|
| Background | Dynamic/colorful | Warm white/cream |
| Card style | Colored fills, bold | White, subtle border |
| Typography | Expressive, large | Clean, readable |
| Spacing | Dense | Generous |
| Shadows | Heavy M3E shadows | Soft, minimal |
| Animation | Bouncy M3E physics | Subtle fade/translate |
| Navigation | Feature-rich | Minimal |
| Overall feel | Playful, modern | Calm, refined |

---

*Document version: 1.0*
*Last updated: 2026-05-29*
