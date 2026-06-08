package com.at210co60.tiku.ui.screen.wrong

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.at210co60.tiku.data.model.QuestionType
import com.at210co60.tiku.data.model.WrongRecordWithQuestion
import com.at210co60.tiku.data.repository.QuestionRepository
import com.at210co60.tiku.ui.components.WarmBadge
import com.at210co60.tiku.ui.components.WarmEmptyState
import com.at210co60.tiku.ui.components.WarmTopBar
import com.at210co60.tiku.ui.theme.AccentError
import com.at210co60.tiku.ui.theme.AccentInfo
import com.at210co60.tiku.ui.theme.AccentPrimary
import com.at210co60.tiku.ui.theme.AccentSuccess
import com.at210co60.tiku.ui.theme.Radius
import com.at210co60.tiku.ui.theme.Spacing
import com.at210co60.tiku.ui.theme.SurfaceColor
import com.at210co60.tiku.ui.theme.TextPrimary
import com.at210co60.tiku.ui.theme.TextSecondary
import com.at210co60.tiku.ui.theme.WarmCream
import com.at210co60.tiku.ui.theme.WarmWhite
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WrongQuestionsScreen(
    repository: QuestionRepository,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val wrongRecords by repository.getWrongRecordsWithQuestions().collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            WarmTopBar(
                title = "错题本",
                onBack = onBack,
            )
        },
        containerColor = WarmWhite,
    ) { padding ->
        if (wrongRecords.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                WarmEmptyState(
                    icon = Icons.Default.LibraryBooks,
                    title = "暂无错题记录",
                    subtitle = "去刷题，答错的题目会自动收录到这里",
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = Spacing.lg),
                verticalArrangement = Arrangement.spacedBy(Spacing.md),
            ) {
                item {
                    Text(
                        text = "共 ${wrongRecords.size} 道错题",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextSecondary,
                        modifier = Modifier.padding(vertical = Spacing.sm),
                    )
                }
                items(wrongRecords, key = { it.record.id }) { record ->
                    WrongQuestionCard(
                        record = record,
                        onDelete = {
                            scope.launch {
                                repository.deleteWrongRecord(record.record.questionId)
                            }
                        },
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(Spacing.lg))
                }
            }
        }
    }
}

@Composable
private fun WrongQuestionCard(
    record: WrongRecordWithQuestion,
    onDelete: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    val typeLabel = when (record.questionType) {
        QuestionType.SINGLE_CHOICE -> "单选题"
        QuestionType.MULTI_CHOICE -> "多选题"
        QuestionType.TRUE_FALSE -> "判断题"
        QuestionType.SHORT_ANSWER -> "简答题"
    }

    val typeColor = when (record.questionType) {
        QuestionType.SINGLE_CHOICE -> AccentPrimary
        QuestionType.MULTI_CHOICE -> AccentSuccess
        QuestionType.TRUE_FALSE -> AccentInfo
        QuestionType.SHORT_ANSWER -> TextSecondary
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(Radius.md),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.md),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f),
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(AccentError),
                    )
                    Spacer(modifier = Modifier.width(Spacing.sm))
                    Text(
                        text = record.questionTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary,
                        maxLines = if (expanded) Int.MAX_VALUE else 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                }
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp),
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "删除",
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(Spacing.sm))

            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                WarmBadge(
                    text = typeLabel,
                    isSuccess = record.questionType == QuestionType.MULTI_CHOICE,
                )
                Text(
                    text = formatTime(record.record.answeredAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(Spacing.md))

                // Options
                if (record.questionOptions.isNotEmpty()) {
                    Text(
                        text = "选项",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary,
                    )
                    Spacer(modifier = Modifier.height(Spacing.xs))
                    record.questionOptions.forEach { option ->
                        val isCorrectOption = option in record.questionAnswers
                        Row(
                            modifier = Modifier.padding(vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = if (isCorrectOption) "✓ " else "  ",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isCorrectOption) AccentSuccess else TextPrimary,
                                fontWeight = if (isCorrectOption) FontWeight.Bold else FontWeight.Normal,
                            )
                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isCorrectOption) AccentSuccess else TextPrimary,
                                fontWeight = if (isCorrectOption) FontWeight.Bold else FontWeight.Normal,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(Spacing.md))
                }

                // Answer comparison
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = AccentError.copy(alpha = 0.08f),
                    ),
                    shape = RoundedCornerShape(Radius.sm),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(modifier = Modifier.padding(Spacing.sm)) {
                        Text(
                            text = "你的答案：${record.record.userAnswer}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AccentError,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.xs))

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = AccentSuccess.copy(alpha = 0.08f),
                    ),
                    shape = RoundedCornerShape(Radius.sm),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(modifier = Modifier.padding(Spacing.sm)) {
                        Text(
                            text = "正确答案：${record.questionAnswers.joinToString(", ")}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AccentSuccess,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }

                // Explanation
                if (record.questionExplanation.isNotBlank()) {
                    Spacer(modifier = Modifier.height(Spacing.md))
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = WarmCream,
                        ),
                        shape = RoundedCornerShape(Radius.sm),
                    ) {
                        Column(modifier = Modifier.padding(Spacing.sm)) {
                            Text(
                                text = "解析",
                                style = MaterialTheme.typography.labelMedium,
                                color = TextSecondary,
                            )
                            Spacer(modifier = Modifier.height(Spacing.xs))
                            Text(
                                text = record.questionExplanation,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextPrimary,
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun formatTime(timestamp: Long): String {
    val date = java.util.Date(timestamp)
    val format = java.text.SimpleDateFormat("MM-dd HH:mm", java.util.Locale.getDefault())
    return format.format(date)
}
