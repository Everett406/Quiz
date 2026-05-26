package com.at210co60.tiku.ui.screen.question

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.at210co60.tiku.data.model.Question
import com.at210co60.tiku.data.model.QuestionType

private val sampleQuestions = listOf(
    Question(
        id = 1,
        title = "Kotlin 中 val 和 var 的区别是什么？",
        type = QuestionType.SHORT_ANSWER,
        options = emptyList(),
        answer = "val 声明不可变变量（只读），var 声明可变变量。",
        tags = listOf("Kotlin", "基础"),
    ),
    Question(
        id = 2,
        title = "Compose 中哪个注解用于标记可组合函数？",
        type = QuestionType.SINGLE_CHOICE,
        options = listOf("@Compose", "@Composable", "@Component", "@View"),
        answer = "@Composable",
        tags = listOf("Jetpack Compose", "基础"),
    ),
    Question(
        id = 3,
        title = "JVM 上的字节码文件扩展名是 .class。",
        type = QuestionType.TRUE_FALSE,
        options = emptyList(),
        answer = "true",
        tags = listOf("JVM", "基础"),
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionListScreen(
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("题目列表") },
                navigationIcon = {
                    Text(
                        text = "← 返回",
                        modifier = Modifier
                            .clickable(onClick = onBack)
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.primary,
                    )
                },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
        ) {
            items(sampleQuestions) { question ->
                QuestionCard(question = question)
            }
        }
    }
}

@Composable
private fun QuestionCard(question: Question) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = question.title,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = question.tags.joinToString(" / "),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}
