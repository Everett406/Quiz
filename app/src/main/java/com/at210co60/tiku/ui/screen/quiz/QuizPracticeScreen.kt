package com.at210co60.tiku.ui.screen.quiz

import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
    onBack: () -> Unit,
) {
    val viewModel: QuizViewModel = viewModel(
        factory = QuizViewModel.Factory(repository)
    )

    val currentQuestion by viewModel.currentQuestion.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val totalQuestions by viewModel.totalQuestions.collectAsState()
    val selectedAnswer by viewModel.selectedAnswer.collectAsState()
    val isAnswered by viewModel.isAnswered.collectAsState()
    val isLastQuestion by viewModel.isLastQuestion.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("刷题练习") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
            )
        },
    ) { padding ->
        if (currentQuestion == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                if (totalQuestions == 0) {
                    Text("暂无题目，请先导入题库", style = MaterialTheme.typography.bodyLarge)
                } else {
                    Text("加载中...", style = MaterialTheme.typography.bodyLarge)
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                // Progress indicator
                Text(
                    text = "${currentIndex + 1} / $totalQuestions",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Linear progress bar (simple colored bar)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(2.dp),
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(
                                fraction = if (totalQuestions > 0) (currentIndex + 1).toFloat() / totalQuestions else 0f
                            )
                            .height(4.dp)
                            .then(
                                if (isAnswered) Modifier.border(
                                    width = 1.dp,
                                    color = if (viewModel.isCorrect()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                                    shape = RoundedCornerShape(2.dp),
                                ) else Modifier
                            )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Question type badge
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

                // Question title
                Text(
                    text = currentQuestion!!.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Answer options based on question type
                when (currentQuestion!!.type) {
                    QuestionType.SINGLE_CHOICE -> {
                        currentQuestion!!.options.forEach { option ->
                            OptionCard(
                                text = option,
                                isSelected = selectedAnswer == option,
                                isCorrect = isAnswered && option == currentQuestion!!.answer,
                                isWrong = isAnswered && selectedAnswer == option && option != currentQuestion!!.answer,
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
                                isSelected = selectedAnswer == option,
                                isCorrect = isAnswered && option == currentQuestion!!.answer,
                                isWrong = isAnswered && selectedAnswer == option && option != currentQuestion!!.answer,
                                isEnabled = !isAnswered,
                                onClick = { viewModel.selectAnswer(option) },
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                    QuestionType.TRUE_FALSE -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            OptionCard(
                                text = "正确",
                                isSelected = selectedAnswer == "true",
                                isCorrect = isAnswered && currentQuestion!!.answer.equals("true", ignoreCase = true) && selectedAnswer == "true",
                                isWrong = isAnswered && selectedAnswer == "true" && !currentQuestion!!.answer.equals("true", ignoreCase = true),
                                isEnabled = !isAnswered,
                                onClick = { viewModel.selectAnswer("true") },
                                modifier = Modifier.weight(1f),
                            )
                            OptionCard(
                                text = "错误",
                                isSelected = selectedAnswer == "false",
                                isCorrect = isAnswered && currentQuestion!!.answer.equals("false", ignoreCase = true) && selectedAnswer == "false",
                                isWrong = isAnswered && selectedAnswer == "false" && !currentQuestion!!.answer.equals("false", ignoreCase = true),
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

                // Answer feedback
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
                            Text(
                                text = if (isCorrect) "✓ 回答正确" else "✗ 回答错误",
                                style = MaterialTheme.typography.titleMedium,
                                color = if (isCorrect)
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                else
                                    MaterialTheme.colorScheme.onErrorContainer,
                            )
                            Text(
                                text = "正确答案：${currentQuestion!!.answer}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(top = 4.dp),
                                color = if (isCorrect)
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                else
                                    MaterialTheme.colorScheme.onErrorContainer,
                            )
                        }
                    }

                    // Explanation
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

                    Spacer(modifier = Modifier.height(16.dp))

                    // Navigation buttons
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
                        if (!isLastQuestion) {
                            Button(
                                onClick = { viewModel.nextQuestion() },
                                modifier = Modifier.weight(1f),
                            ) {
                                Text("下一题")
                            }
                        } else {
                            Button(
                                onClick = { viewModel.resetQuiz() },
                                modifier = Modifier.weight(1f),
                            ) {
                                Text("重新开始")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
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
            .border(1.dp, borderColor, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        enabled = isEnabled,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp),
        )
    }
}
