package com.at210co60.tiku.ui.screen.quiz

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.at210co60.tiku.data.model.QuestionType
import com.at210co60.tiku.data.repository.QuestionRepository
import com.at210co60.tiku.ui.components.WarmButton
import com.at210co60.tiku.ui.components.WarmEmptyState
import com.at210co60.tiku.ui.components.WarmProgressBar
import com.at210co60.tiku.ui.components.WarmSecondaryButton
import com.at210co60.tiku.ui.components.WarmTopBar
import com.at210co60.tiku.ui.theme.AccentError
import com.at210co60.tiku.ui.theme.AccentPrimary
import com.at210co60.tiku.ui.theme.AccentSuccess
import com.at210co60.tiku.ui.theme.Radius
import com.at210co60.tiku.ui.theme.Spacing
import com.at210co60.tiku.ui.theme.SurfaceColor
import com.at210co60.tiku.ui.theme.TextPrimary
import com.at210co60.tiku.ui.theme.TextSecondary
import com.at210co60.tiku.ui.theme.WarmCream
import com.at210co60.tiku.ui.theme.WarmWhite
import com.at210co60.tiku.viewmodel.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizPracticeScreen(
    repository: QuestionRepository,
    mode: String = "sequential",
    bankId: Long = 0,
    onBack: () -> Unit,
) {
    val viewModel: QuizViewModel = viewModel(
        factory = QuizViewModel.Factory(repository, mode, bankId)
    )

    val currentQuestion by viewModel.currentQuestion.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val totalQuestions by viewModel.totalQuestions.collectAsState()
    val selectedAnswers by viewModel.selectedAnswers.collectAsState()
    val isAnswered by viewModel.isAnswered.collectAsState()
    val isLastQuestion by viewModel.isLastQuestion.collectAsState()
    val quizCompleted by viewModel.quizCompleted.collectAsState()
    val correctCount by viewModel.correctCount.collectAsState()
    val totalAnswered by viewModel.totalAnswered.collectAsState()

    val titleText = when (mode) {
        "random" -> "随机刷题"
        "exam" -> "模拟考试"
        else -> "顺序刷题"
    }

    LaunchedEffect(isAnswered, selectedAnswers, currentQuestion) {
        if (isAnswered && selectedAnswers.isNotEmpty() && currentQuestion != null) {
            val userAnswer = when (currentQuestion!!.type) {
                QuestionType.MULTI_CHOICE -> selectedAnswers.sorted().joinToString(", ")
                else -> selectedAnswers.firstOrNull() ?: ""
            }
            repository.recordAnswer(
                questionId = currentQuestion!!.id,
                bankId = bankId,
                userAnswer = userAnswer,
                isCorrect = viewModel.isCorrect(),
                practiceMode = mode,
            )
        }
    }

    Scaffold(
        topBar = {
            WarmTopBar(
                title = titleText,
                onBack = onBack,
            )
        },
        containerColor = WarmWhite,
    ) { padding ->
        when {
            quizCompleted -> {
                QuizCompletedScreen(
                    correctCount = correctCount,
                    totalAnswered = totalAnswered,
                    onRestart = { viewModel.resetQuiz() },
                    onBack = onBack,
                    modifier = Modifier.padding(padding),
                )
            }
            currentQuestion == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center,
                ) {
                    if (totalQuestions == 0) {
                        WarmEmptyState(
                            icon = Icons.AutoMirrored.Filled.ArrowBack,
                            title = "暂无题目",
                            subtitle = "请先导入题库",
                            action = {
                                WarmSecondaryButton(
                                    text = "返回",
                                    onClick = onBack,
                                )
                            },
                        )
                    } else {
                        Text("加载中...", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = Spacing.lg)
                        .verticalScroll(rememberScrollState()),
                ) {
                    Spacer(modifier = Modifier.height(Spacing.md))

                    // Progress
                    Text(
                        text = "${currentIndex + 1} / $totalQuestions",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextSecondary,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.height(Spacing.sm))

                    WarmProgressBar(
                        progress = if (totalQuestions > 0) (currentIndex + 1).toFloat() / totalQuestions else 0f,
                    )

                    Spacer(modifier = Modifier.height(Spacing.lg))

                    // Question Type Badge
                    val typeLabel = when (currentQuestion!!.type) {
                        QuestionType.SINGLE_CHOICE -> "单选题"
                        QuestionType.MULTI_CHOICE -> "多选题"
                        QuestionType.TRUE_FALSE -> "判断题"
                        QuestionType.SHORT_ANSWER -> "简答题"
                    }
                    Text(
                        text = typeLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = AccentPrimary,
                    )

                    Spacer(modifier = Modifier.height(Spacing.sm))

                    // Question Title
                    Text(
                        text = currentQuestion!!.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                    )

                    Spacer(modifier = Modifier.height(Spacing.lg))

                    // Options
                    when (currentQuestion!!.type) {
                        QuestionType.SINGLE_CHOICE -> {
                            currentQuestion!!.options.forEach { option ->
                                QuizOptionItem(
                                    text = option,
                                    isSelected = option in selectedAnswers,
                                    isCorrect = isAnswered && option == currentQuestion!!.answers.firstOrNull(),
                                    isWrong = isAnswered && selectedAnswers.contains(option) && option != currentQuestion!!.answers.firstOrNull(),
                                    isEnabled = !isAnswered,
                                    onClick = { viewModel.selectAnswer(option) },
                                )
                                Spacer(modifier = Modifier.height(Spacing.sm))
                            }
                        }
                        QuestionType.MULTI_CHOICE -> {
                            currentQuestion!!.options.forEach { option ->
                                QuizOptionItem(
                                    text = option,
                                    isSelected = option in selectedAnswers,
                                    isCorrect = isAnswered && option in currentQuestion!!.answers,
                                    isWrong = isAnswered && option in selectedAnswers && option !in currentQuestion!!.answers,
                                    isEnabled = !isAnswered,
                                    onClick = { viewModel.selectAnswer(option) },
                                )
                                Spacer(modifier = Modifier.height(Spacing.sm))
                            }
                            if (!isAnswered) {
                                Spacer(modifier = Modifier.height(Spacing.sm))
                                WarmButton(
                                    text = "确认答案",
                                    onClick = { viewModel.confirmMultiChoiceAnswer() },
                                    enabled = selectedAnswers.isNotEmpty(),
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        }
                        QuestionType.TRUE_FALSE -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(Spacing.md),
                            ) {
                                QuizOptionItem(
                                    text = "正确",
                                    isSelected = "true" in selectedAnswers,
                                    isCorrect = isAnswered && currentQuestion!!.answers.firstOrNull()?.equals("true", ignoreCase = true) == true && "true" in selectedAnswers,
                                    isWrong = isAnswered && "true" in selectedAnswers && currentQuestion!!.answers.firstOrNull()?.equals("true", ignoreCase = true) != true,
                                    isEnabled = !isAnswered,
                                    onClick = { viewModel.selectAnswer("true") },
                                    modifier = Modifier.weight(1f),
                                )
                                QuizOptionItem(
                                    text = "错误",
                                    isSelected = "false" in selectedAnswers,
                                    isCorrect = isAnswered && currentQuestion!!.answers.firstOrNull()?.equals("false", ignoreCase = true) == true && "false" in selectedAnswers,
                                    isWrong = isAnswered && "false" in selectedAnswers && currentQuestion!!.answers.firstOrNull()?.equals("false", ignoreCase = true) != true,
                                    isEnabled = !isAnswered,
                                    onClick = { viewModel.selectAnswer("false") },
                                    modifier = Modifier.weight(1f),
                                )
                            }
                        }
                        QuestionType.SHORT_ANSWER -> {
                            var inputText by remember { mutableStateOf("") }
                            OutlinedTextField(
                                value = inputText,
                                onValueChange = { if (!isAnswered) inputText = it },
                                label = { Text("请输入答案") },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = !isAnswered,
                                singleLine = true,
                                shape = RoundedCornerShape(Radius.md),
                            )
                            Spacer(modifier = Modifier.height(Spacing.sm))
                            if (!isAnswered) {
                                WarmButton(
                                    text = "提交答案",
                                    onClick = { viewModel.selectAnswer(inputText) },
                                    enabled = inputText.isNotBlank(),
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        }
                    }

                    if (isAnswered) {
                        Spacer(modifier = Modifier.height(Spacing.lg))

                        // Result Card
                        val isCorrect = viewModel.isCorrect()
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (isCorrect) AccentSuccess.copy(alpha = 0.1f) else AccentError.copy(alpha = 0.1f),
                            ),
                            shape = RoundedCornerShape(Radius.md),
                            border = BorderStroke(1.dp, if (isCorrect) AccentSuccess else AccentError),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(modifier = Modifier.padding(Spacing.md)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (isCorrect) Icons.Default.Check else Icons.Default.Close,
                                        contentDescription = null,
                                        tint = if (isCorrect) AccentSuccess else AccentError,
                                        modifier = Modifier.size(24.dp),
                                    )
                                    Spacer(modifier = Modifier.width(Spacing.sm))
                                    Text(
                                        text = if (isCorrect) "回答正确！" else "回答错误",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (isCorrect) AccentSuccess else AccentError,
                                    )
                                }
                                Spacer(modifier = Modifier.height(Spacing.sm))
                                Text(
                                    text = "正确答案：${currentQuestion!!.answers.joinToString(", ")}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextPrimary,
                                )
                            }
                        }

                        if (currentQuestion!!.explanation.isNotBlank()) {
                            Spacer(modifier = Modifier.height(Spacing.sm))
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = WarmCream,
                                ),
                                shape = RoundedCornerShape(Radius.md),
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Column(modifier = Modifier.padding(Spacing.md)) {
                                    Text(
                                        text = "解析",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = TextSecondary,
                                    )
                                    Spacer(modifier = Modifier.height(Spacing.xs))
                                    Text(
                                        text = currentQuestion!!.explanation,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextPrimary,
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(Spacing.lg))

                        // Navigation Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(Spacing.md),
                        ) {
                            if (currentIndex > 0) {
                                WarmSecondaryButton(
                                    text = "上一题",
                                    onClick = { viewModel.previousQuestion() },
                                    modifier = Modifier.weight(1f),
                                )
                            }
                            if (isLastQuestion) {
                                WarmButton(
                                    text = "查看结果",
                                    onClick = { viewModel.finishQuiz() },
                                    modifier = Modifier.weight(1f),
                                )
                            } else {
                                WarmButton(
                                    text = "下一题",
                                    onClick = { viewModel.nextQuestion() },
                                    modifier = Modifier.weight(1f),
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(Spacing.lg))
                }
            }
        }
    }
}

@Composable
private fun QuizOptionItem(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    isWrong: Boolean,
    isEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = when {
        isCorrect -> AccentSuccess.copy(alpha = 0.12f)
        isWrong -> AccentError.copy(alpha = 0.12f)
        isSelected -> AccentPrimary.copy(alpha = 0.12f)
        else -> SurfaceColor
    }

    val borderColor = when {
        isCorrect -> AccentSuccess
        isWrong -> AccentError
        isSelected -> AccentPrimary
        else -> WarmCream
    }

    val textColor = when {
        isCorrect -> AccentSuccess
        isWrong -> AccentError
        else -> TextPrimary
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Radius.md),
        color = backgroundColor,
        border = BorderStroke(
            width = if (isSelected || isCorrect || isWrong) 1.5.dp else 1.dp,
            color = borderColor,
        ),
        onClick = { if (isEnabled) onClick() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor,
                modifier = Modifier.weight(1f),
            )
            if (isCorrect) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "正确",
                    tint = AccentSuccess,
                    modifier = Modifier.size(20.dp),
                )
            } else if (isWrong) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "错误",
                    tint = AccentError,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}

@Composable
private fun QuizCompletedScreen(
    correctCount: Int,
    totalAnswered: Int,
    onRestart: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accuracy = if (totalAnswered > 0) (correctCount * 100 / totalAnswered) else 0

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "🎉",
            style = MaterialTheme.typography.displayLarge,
        )

        Spacer(modifier = Modifier.height(Spacing.lg))

        Text(
            text = "刷题完成！",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
        )

        Spacer(modifier = Modifier.height(Spacing.xl))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = AccentPrimary.copy(alpha = 0.1f),
            ),
            shape = RoundedCornerShape(Radius.lg),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.lg),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "正确率",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextSecondary,
                )
                Spacer(modifier = Modifier.height(Spacing.xs))
                Text(
                    text = "$accuracy%",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = AccentPrimary,
                )
                Spacer(modifier = Modifier.height(Spacing.sm))
                Text(
                    text = "正确 $correctCount / 共 $totalAnswered 题",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary,
                )
            }
        }

        Spacer(modifier = Modifier.height(Spacing.xl))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            WarmSecondaryButton(
                text = "返回题库",
                onClick = onBack,
                modifier = Modifier.weight(1f),
            )
            WarmButton(
                text = "再来一次",
                onClick = onRestart,
                modifier = Modifier.weight(1f),
            )
        }
    }
}
