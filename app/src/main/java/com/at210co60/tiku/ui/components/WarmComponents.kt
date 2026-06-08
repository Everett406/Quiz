package com.at210co60.tiku.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.at210co60.tiku.ui.theme.AccentError
import com.at210co60.tiku.ui.theme.AccentPrimary
import com.at210co60.tiku.ui.theme.AccentSuccess
import com.at210co60.tiku.ui.theme.Radius
import com.at210co60.tiku.ui.theme.Spacing
import com.at210co60.tiku.ui.theme.TextPrimary
import com.at210co60.tiku.ui.theme.TextSecondary
import com.at210co60.tiku.ui.theme.WarmCream
import com.at210co60.tiku.ui.theme.WarmWhite

/**
 * Warm Primary Button - 暖色主按钮
 */
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
        modifier = modifier.height(52.dp),
        enabled = enabled,
        shape = RoundedCornerShape(Radius.lg),
        colors = ButtonDefaults.buttonColors(
            containerColor = AccentPrimary,
            contentColor = Color.White,
            disabledContainerColor = AccentPrimary.copy(alpha = 0.4f),
            disabledContentColor = Color.White.copy(alpha = 0.6f),
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
        ),
    ) {
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(Spacing.sm))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
        )
    }
}

/**
 * Warm Secondary Button - 暖色次要按钮（描边样式）
 */
@Composable
fun WarmSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        enabled = enabled,
        shape = RoundedCornerShape(Radius.lg),
        border = BorderStroke(1.5.dp, if (enabled) AccentPrimary else AccentPrimary.copy(alpha = 0.4f)),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = AccentPrimary,
            disabledContentColor = AccentPrimary.copy(alpha = 0.4f),
        ),
    ) {
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(Spacing.sm))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
        )
    }
}

/**
 * Warm Card - 暖色卡片
 */
@Composable
fun WarmCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    if (onClick != null) {
        Card(
            modifier = modifier,
            onClick = onClick,
            shape = RoundedCornerShape(Radius.md),
            colors = CardDefaults.cardColors(containerColor = WarmCream),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        ) {
            content()
        }
    } else {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(Radius.md),
            colors = CardDefaults.cardColors(containerColor = WarmCream),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        ) {
            content()
        }
    }
}

/**
 * Warm Option Card - 答题选项卡片
 */
@Composable
fun WarmOptionCard(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    isWrong: Boolean,
    isEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showCorrectIndicator: Boolean = false,
) {
    val backgroundColor = when {
        isCorrect -> AccentSuccess.copy(alpha = 0.15f)
        isWrong -> AccentError.copy(alpha = 0.15f)
        isSelected -> AccentPrimary.copy(alpha = 0.12f)
        else -> WarmWhite
    }

    val borderColor = when {
        isCorrect -> AccentSuccess
        isWrong -> AccentError
        isSelected -> AccentPrimary
        else -> Color(0xFFE0DDD8)
    }

    val textColor = when {
        isCorrect -> AccentSuccess
        isWrong -> AccentError
        else -> TextPrimary
    }

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(Radius.md))
            .then(
                if (onClick != null) Modifier else Modifier
            ),
        shape = RoundedCornerShape(Radius.md),
        color = backgroundColor,
        border = BorderStroke(
            width = if (isSelected || isCorrect || isWrong) 1.5.dp else 1.dp,
            color = borderColor,
        ),
        onClick = if (isEnabled && onClick != null) onClick else null,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor,
                modifier = Modifier.weight(1f),
            )
            if (showCorrectIndicator && isCorrect) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "正确",
                    tint = AccentSuccess,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}

/**
 * Warm Progress Bar - 简洁进度条
 */
@Composable
fun WarmProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    LinearProgressIndicator(
        progress = { progress.coerceIn(0f, 1f) },
        modifier = modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(Radius.full)),
        color = AccentPrimary,
        trackColor = WarmCream,
    )
}

/**
 * Warm Top Bar - 简洁顶部导航栏
 */
@Composable
fun WarmTopBar(
    title: String,
    onBack: (() -> Unit)? = null,
    actions: @Composable () -> Unit = {},
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = WarmWhite,
        shadowElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "返回",
                        tint = TextPrimary,
                    )
                }
            }
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = if (onBack != null) 0.dp else Spacing.md),
            )
            actions()
        }
    }
}

/**
 * Warm Section Header - 区域标题
 */
@Composable
fun WarmSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = TextSecondary,
        modifier = modifier.padding(vertical = Spacing.sm),
    )
}

/**
 * Warm Empty State - 空状态提示
 */
@Composable
fun WarmEmptyState(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = TextSecondary.copy(alpha = 0.4f),
        )
        Spacer(modifier = Modifier.height(Spacing.md))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = TextSecondary,
        )
        Spacer(modifier = Modifier.height(Spacing.xs))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary.copy(alpha = 0.7f),
        )
        if (action != null) {
            Spacer(modifier = Modifier.height(Spacing.lg))
            action()
        }
    }
}

/**
 * Warm Badge - 徽章/标签
 */
@Composable
fun WarmBadge(
    text: String,
    modifier: Modifier = Modifier,
    isSuccess: Boolean = false,
    isError: Boolean = false,
) {
    val backgroundColor = when {
        isSuccess -> AccentSuccess.copy(alpha = 0.15f)
        isError -> AccentError.copy(alpha = 0.15f)
        else -> AccentPrimary.copy(alpha = 0.15f)
    }
    val textColor = when {
        isSuccess -> AccentSuccess
        isError -> AccentError
        else -> AccentPrimary
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(Radius.full),
        color = backgroundColor,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = textColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
        )
    }
}

/**
 * Warm Stat Card - 统计卡片
 */
@Composable
fun WarmStatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    isHighlight: Boolean = false,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(Radius.md),
        colors = CardDefaults.cardColors(
            containerColor = if (isHighlight) AccentPrimary.copy(alpha = 0.1f) else WarmCream
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier.padding(Spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = if (isHighlight) AccentPrimary else TextPrimary,
            )
            Spacer(modifier = Modifier.height(Spacing.xs))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
            )
        }
    }
}
