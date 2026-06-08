package com.at210co60.tiku.ui.screen.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.at210co60.tiku.data.model.BankStats
import com.at210co60.tiku.data.repository.QuestionRepository
import com.at210co60.tiku.ui.components.WarmTopBar
import com.at210co60.tiku.ui.theme.AccentError
import com.at210co60.tiku.ui.theme.AccentInfo
import com.at210co60.tiku.ui.theme.AccentPrimary
import com.at210co60.tiku.ui.theme.AccentSuccess
import com.at210co60.tiku.ui.theme.Radius
import com.at210co60.tiku.ui.theme.Spacing
import com.at210co60.tiku.ui.theme.TextPrimary
import com.at210co60.tiku.ui.theme.TextSecondary
import com.at210co60.tiku.ui.theme.WarmWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizDetailScreen(
    bankId: Long,
    title: String,
    repository: QuestionRepository,
    onBack: () -> Unit,
    onNavigateToPractice: (String) -> Unit,
) {
    var stats by remember { mutableStateOf<BankStats?>(null) }

    LaunchedEffect(bankId) {
        stats = repository.getBankStatsSnapshot(bankId)
    }

    Scaffold(
        topBar = {
            WarmTopBar(
                title = title,
                onBack = onBack,
            )
        },
        containerColor = WarmWhite,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = Spacing.lg)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(Spacing.md))

            // Action Cards Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.md),
            ) {
                WarmActionCard(
                    title = "顺序刷题",
                    subtitle = "按原题顺序系统学习",
                    icon = Icons.Default.Book,
                    accentColor = AccentPrimary,
                    onClick = { onNavigateToPractice("sequential") },
                    modifier = Modifier.weight(1f),
                )
                WarmActionCard(
                    title = "随机刷题",
                    subtitle = "打乱顺序模拟实战",
                    icon = Icons.Default.Casino,
                    accentColor = AccentSuccess,
                    onClick = { onNavigateToPractice("random") },
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(modifier = Modifier.height(Spacing.md))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.md),
            ) {
                WarmActionCard(
                    title = "模拟考试",
                    subtitle = "自定义题数·80分及格",
                    icon = Icons.Default.EditNote,
                    accentColor = AccentInfo,
                    onClick = { onNavigateToPractice("exam") },
                    modifier = Modifier.weight(1f),
                )
                WarmActionCard(
                    title = "错题本",
                    subtitle = "针对薄弱点重点突破",
                    icon = Icons.Default.ErrorOutline,
                    accentColor = AccentError,
                    onClick = { onNavigateToPractice("wrong") },
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(modifier = Modifier.height(Spacing.lg))

            // Stats Card
            WarmStatsCard(stats = stats)
        }
    }
}

@Composable
private fun WarmActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .height(120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(Radius.md),
        colors = CardDefaults.cardColors(containerColor = accentColor.copy(alpha = 0.1f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(32.dp),
                tint = accentColor,
            )
            Spacer(modifier = Modifier.height(Spacing.sm))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                textAlign = TextAlign.Center,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun WarmStatsCard(stats: BankStats?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Radius.md),
        colors = CardDefaults.cardColors(containerColor = WarmWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "${stats?.totalQuestions ?: 0} 题",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
            )
            if ((stats?.wrongAnswers ?: 0) > 0) {
                Spacer(modifier = Modifier.height(Spacing.xs))
                Text(
                    text = "已收录 ${stats?.wrongAnswers} 道错题",
                    style = MaterialTheme.typography.bodySmall,
                    color = AccentError,
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.md)
                .padding(bottom = Spacing.lg),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            WarmStatItem(
                value = "${stats?.answeredQuestions ?: 0}",
                label = "已做题",
                accentColor = AccentPrimary,
            )
            WarmStatItem(
                value = "${stats?.correctRate ?: 0}%",
                label = "正确率",
                accentColor = AccentSuccess,
            )
            WarmStatItem(
                value = "${stats?.wrongAnswers ?: 0}",
                label = "错题",
                accentColor = AccentError,
            )
            WarmStatItem(
                value = if ((stats?.answeredQuestions ?: 0) >= 5 && (stats?.correctRate ?: 0) >= 80) "通过" else "--",
                label = "考试",
                accentColor = AccentInfo,
            )
        }
    }
}

@Composable
private fun WarmStatItem(
    value: String,
    label: String,
    accentColor: Color,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = accentColor,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
        )
    }
}
