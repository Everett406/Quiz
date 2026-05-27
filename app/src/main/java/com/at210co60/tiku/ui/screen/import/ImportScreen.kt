package com.at210co60.tiku.ui.screen.import

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.at210co60.tiku.data.repository.QuestionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportScreen(
    repository: QuestionRepository,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var importStatus by remember { mutableStateOf<String?>(null) }
    var isImporting by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            scope.launch {
                isImporting = true
                importStatus = "正在导入..."
                try {
                    val jsonString = readJsonFromUri(context, it)
                    repository.clearAndImport(jsonString)
                    importStatus = "导入成功！"
                } catch (e: Exception) {
                    importStatus = "导入失败：${e.message}"
                }
                isImporting = false
            }
        }
    }

    LaunchedEffect(importStatus) {
        if (importStatus != null && !isImporting) {
            Toast.makeText(context, importStatus, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("导入题库") },
                navigationIcon = {
                    Text(
                        text = "← 返回",
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable(onClick = onBack),
                        color = MaterialTheme.colorScheme.primary,
                    )
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.Default.UploadFile,
                contentDescription = null,
                modifier = Modifier.padding(bottom = 16.dp),
                tint = MaterialTheme.colorScheme.primary,
            )

            Text(
                text = "从 JSON 文件导入题目",
                style = MaterialTheme.typography.titleLarge,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "选择一个 JSON 文件，将题目批量导入到本地数据库。\n导入后会清除已有的题目数据。",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { launcher.launch("application/json") },
                enabled = !isImporting,
                modifier = Modifier.fillMaxWidth(0.6f),
            ) {
                Text(if (isImporting) "导入中..." else "选择 JSON 文件")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    scope.launch {
                        isImporting = true
                        importStatus = "正在加载示例数据..."
                        try {
                            val jsonString = loadSampleQuestions(context)
                            repository.clearAndImport(jsonString)
                            importStatus = "示例数据加载成功！"
                        } catch (e: Exception) {
                            importStatus = "加载失败：${e.message}"
                        }
                        isImporting = false
                    }
                },
                enabled = !isImporting,
                modifier = Modifier.fillMaxWidth(0.6f),
            ) {
                Text("加载内置示例数据")
            }

            if (importStatus != null) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = importStatus!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (importStatus!!.contains("成功"))
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

private suspend fun readJsonFromUri(context: Context, uri: Uri): String = withContext(Dispatchers.IO) {
    context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
        ?: throw IllegalArgumentException("无法读取文件")
}

private suspend fun loadSampleQuestions(context: Context): String = withContext(Dispatchers.IO) {
    context.assets.open("sample_questions.json").bufferedReader().use { it.readText() }
}
