package com.at210co60.tiku.ui.screen.quiz

import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
            TopAppBar(
                title = {
                    val titleText = when (mode) {
                        "random" -> "随机刷题"
                        "exam" -> "模拟考试"
                        else -> "顺序刷题"
                    }
                    Text(titleText)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
            )
        },
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
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("暂无题目，请先导入题库", style = MaterialTheme.typography.bodyLarge)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = onBack) {
                                Text("返回")
                            }
                        }
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
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    Text(
                        text = "${currentIndex + 1} / $totalQuestions",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { if (totalQuestions > 0) (currentIndex + 1).toFloat() / totalQuestions else 0f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    val typeLabel = when (currentQuestion!!.type) {
                        QuestionType.SINGLE_CHOICE -> "单选题"
                        QuestionType.MULTI_CHOICE -> "多选题"
                        QuestionType.TRUE_FALSE -> "判断题"
                        QuestionType.SHORT_ANSWER -> "简答题"
                    }
                    Text(
                        text = typeLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = currentQuestion!!.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    when (currentQuestion!!.type) {
                        QuestionType.SINGLE_CHOICE -> {
                            currentQuestion!!.options.forEach { option ->
                                OptionCard(
                                    text = option,
                                    isSelected = option in selectedAnswers,
                                    isCorrect = isAnswered && option == currentQuestion!!.answers.firstOrNull(),
                                    isWrong = isAnswered && selectedAnswers.contains(option) && option != currentQuestion!!.answers.firstOrNull(),
                                    isEnabled = !isAnswered,
                                    onClick = { viewModel.selectAnswer(option) },
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                        QuestionType.MULTI_CHOICE -> {
                            currentQuestion!!.options.forEach { option ->
                                OptionCard(
                                    text = option,
                                    isSelected = option in selectedAnswers,
                                    isCorrect = isAnswered && option in currentQuestion!!.answers,
                                    isWrong = isAnswered && option in selectedAnswers && option !in currentQuestion!!.answers,
                                    isEnabled = !isAnswered,
                                    onClick = { viewModel.selectAnswer(option) },
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            if (!isAnswered) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = { viewModel.confirmMultiChoiceAnswer() },
                                    enabled = selectedAnswers.isNotEmpty(),
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Text("确认答案")
                                }
                            }
                        }
                        QuestionType.TRUE_FALSE -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                OptionCard(
                                    text = "正确",
                                    isSelected = "true" in selectedAnswers,
                                    isCorrect = isAnswered && currentQuestion!!.answers.firstOrNull()?.equals("true", ignoreCase = true) == true && "true" in selectedAnswers,
                                    isWrong = isAnswered && "true" in selectedAnswers && currentQuestion!!.answers.firstOrNull()?.equals("true", ignoreCase = true) != true,
                                    isEnabled = !isAnswered,
                                    onClick = { viewModel.selectAnswer("true") },
                                    modifier = Modifier.weight(1f),
                                )
                                OptionCard(
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
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            if (!isAnswered) {
                                Button(
                                    onClick = { viewModel.selectAnswer(inputText) },
                                    enabled = inputText.isNotBlank(),
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Text("提交答案")
                                }
                            }
                        }
                    }

                    if (isAnswered) {
                        Spacer(modifier = Modifier.height(24.dp))
                        val isCorrect = viewModel.isCorrect()
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (isCorrect)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.errorContainer,
                            ),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = if (isCorrect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(24.dp),
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isCorrect) "回答正确！" else "回答错误",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (isCorrect)
                                            MaterialTheme.colorScheme.onPrimaryContainer
                                        else
                                            MaterialTheme.colorScheme.onErrorContainer,
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "正确答案：${currentQuestion!!.answers.joinToString(", ")}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isCorrect)
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                    else
                                        MaterialTheme.colorScheme.onErrorContainer,
                                )
                            }
                        }

                        if (currentQuestion!!.explanation.isNotBlank()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                ),
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "解析",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = currentQuestion!!.explanation,
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            if (currentIndex > 0) {
                                OutlinedButton(
                                    onClick = { viewModel.previousQuestion() },
                                    modifier = Modifier.weight(1f),
                                ) {
                                    Text("上一题")
                                }
                            }
                            if (isLastQuestion) {
                                Button(
                                    onClick = { viewModel.finishQuiz() },
                                    modifier = Modifier.weight(1f),
                                ) {
                                    Text("查看结果")
                                }
                            } else {
                                Button(
                                    onClick = { viewModel.nextQuestion() },
                                    modifier = Modifier.weight(1f),
                                ) {
                                    Text("下一题")
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
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
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "\uD83C\uDF89",
            style = MaterialTheme.typography.displayLarge,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "刷题完成！",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "正确率",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    text = "$accuracy%",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "正确 $correctCount / 共 $totalAnswered 题",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f),
            ) {
                Text("返回题库")
            }
            Button(
                onClick = onRestart,
                modifier = Modifier.weight(1f),
            ) {
                Text("再来一次")
            }
        }
    }
}

@Composable
private fun OptionCard(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    isWrong: Boolean,
    isEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = when {
        isCorrect -> MaterialTheme.colorScheme.primaryContainer
        isWrong -> MaterialTheme.colorScheme.errorContainer
        isSelected -> MaterialTheme.colorScheme.secondaryContainer
        else -> MaterialTheme.colorScheme.surface
    }
    val borderColor = when {
        isCorrect -> MaterialTheme.colorScheme.primary
        isWrong -> MaterialTheme.colorScheme.error
        isSelected -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.outline
    }

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .border(2.dp, borderColor, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        enabled = isEnabled,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = borderColor,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}
