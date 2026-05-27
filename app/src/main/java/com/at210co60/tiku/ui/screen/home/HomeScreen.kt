package com.at210co60.tiku.ui.screen.home

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.at210co60.tiku.data.model.QuestionBank
import com.at210co60.tiku.data.repository.QuestionRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    repository: QuestionRepository,
    onNavigateToDetail: (Long, String) -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val questionBanks by repository.getAllQuestionBanksWithCount().collectAsState(initial = emptyList())
    var showMenu by remember { mutableStateOf<Long?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Tiku", fontWeight = FontWeight.Bold)
                },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "设置")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            // Create default bank and import sample data
                            val bankId = repository.insertQuestionBank(
                                com.at210co60.tiku.data.model.QuestionBank(
                                    name = "Kotlin 基础题库",
                                    description = "Kotlin 基础知识点练习",
                                    isDefault = true,
                                )
                            )
                            val sampleJson = """[
                                {"title":"Kotlin 中 val 和 var 的区别是什么？","type":"SINGLE_CHOICE","options":["val 是可变的，var 是不可变的","val 是不可变的，var 是可变的","两者没有区别","val 只能用于基本类型"],"answer":"val 是不可变的，var 是可变的","explanation":"val 声明的变量一旦赋值就不能再修改（只读），var 声明的变量可以随时重新赋值。","tags":["Kotlin","基础"]},
                                {"title":"Compose 中哪个注解用于标记可组合函数？","type":"SINGLE_CHOICE","options":["@Compose","@Composable","@Component","@View"],"answer":"@Composable","explanation":"@Composable 注解用于标记一个函数为可组合函数，使其可以被 Compose 运行时管理和调用。","tags":["Jetpack Compose","基础"]},
                                {"title":"JVM 上的字节码文件扩展名是 .class。","type":"TRUE_FALSE","options":[],"answer":"true","explanation":"Java/Kotlin 编译后生成的字节码文件扩展名确实是 .class。","tags":["JVM","基础"]},
                                {"title":"Android 中 Activity 的生命周期方法正确的顺序是？","type":"SINGLE_CHOICE","options":["onCreate → onStart → onResume → onPause → onStop → onDestroy","onCreate → onResume → onStart → onPause → onStop → onDestroy","onStart → onCreate → onResume → onPause → onStop → onDestroy","onCreate → onStart → onPause → onResume → onStop → onDestroy"],"answer":"onCreate → onStart → onResume → onPause → onStop → onDestroy","explanation":"Activity 的标准生命周期顺序为：onCreate() → onStart() → onResume() → onPause() → onStop() → onDestroy()。","tags":["Android","基础"]},
                                {"title":"以下哪些是 Kotlin 的基本数据类型？（多选）","type":"MULTI_CHOICE","options":["Int","String","Boolean","Double"],"answer":"Int","explanation":"Kotlin 的基本数据类型包括 Int、Long、Float、Double、Boolean、Char、Byte、Short。String 是引用类型。","tags":["Kotlin","基础"]},
                                {"title":"RecyclerView 的作用是什么？","type":"SHORT_ANSWER","options":[],"answer":"用于高效显示大量数据列表的组件，通过复用 ViewHolder 来优化性能。","explanation":"RecyclerView 是 Android 中用于展示大量数据的列表组件，通过 ViewHolder 复用机制大幅提升滚动性能。","tags":["Android","UI"]},
                                {"title":"Kotlin 中空安全（Null Safety）的符号是？","type":"SINGLE_CHOICE","options":["!","?","&","#"],"answer":"?","explanation":"在 Kotlin 中，? 用于标记类型可为空，例如 String? 表示可以为 null 的字符串。","tags":["Kotlin","基础"]},
                                {"title":"Jetpack Compose 是声明式 UI 框架。","type":"TRUE_FALSE","options":[],"answer":"true","explanation":"Jetpack Compose 是 Android 的现代声明式 UI 工具包，开发者通过描述 UI 应该是什么样子来构建界面。","tags":["Jetpack Compose","基础"]},
                                {"title":"Room 数据库属于哪种架构组件？","type":"SINGLE_CHOICE","options":["ViewModel","LiveData","Database","Navigation"],"answer":"Database","explanation":"Room 是 Android Jetpack 中的数据库组件，它是对 SQLite 的抽象层，简化了数据库操作。","tags":["Android","架构"]},
                                {"title":"MVVM 架构中的 VM 代表什么？","type":"SINGLE_CHOICE","options":["View Manager","View Model","Virtual Machine","Value Mapping"],"answer":"View Model","explanation":"MVVM 中的 VM 代表 ViewModel，它负责管理 UI 数据和业务逻辑，将 View 和 Model 分离。","tags":["Android","架构"]},
                                {"title":"Android 中 Intent 的作用是什么？","type":"SHORT_ANSWER","options":[],"answer":"用于在不同组件之间进行通信和跳转，可以启动 Activity、Service 或发送广播。","explanation":"Intent 是 Android 中组件间通信的核心机制，支持显式和隐式两种调用方式。","tags":["Android","基础"]},
                                {"title":"Kotlin 协程中 launch 和 async 的区别是？","type":"SINGLE_CHOICE","options":["launch 返回 Deferred，async 返回 Job","launch 返回 Job 不返回结果，async 返回 Deferred 可以获取结果","两者完全相同","async 只能在主线程使用"],"answer":"launch 返回 Job 不返回结果，async 返回 Deferred 可以获取结果","explanation":"launch 用于启动不返回结果的协程（返回 Job），async 用于启动并返回结果的协程（返回 Deferred），可以通过 await() 获取结果。","tags":["Kotlin","进阶"]},
                                {"title":"Gradle 是一种编程语言。","type":"TRUE_FALSE","options":[],"answer":"false","explanation":"Gradle 是一个构建自动化工具，不是编程语言。它使用 Groovy 或 Kotlin DSL 作为构建脚本语言。","tags":["Android","工具"]},
                                {"title":"Material Design 3 Expressive 首次亮相于哪个 Android 版本？","type":"SINGLE_CHOICE","options":["Android 14","Android 15","Android 16","Android 13"],"answer":"Android 16","explanation":"Material 3 Expressive (M3E) 是 Google 最新一代设计语言，首次亮相于 Android 16。","tags":["设计","Material Design"]},
                                {"title":"以下哪些是 Jetpack Compose 的核心原则？（多选）","type":"MULTI_CHOICE","options":["声明式编程","组合优于继承","状态驱动 UI","命令式编程"],"answer":"声明式编程","explanation":"Jetpack Compose 的核心原则包括：声明式编程（描述 UI 而非操作 UI）、组合优于继承（通过组合小组件构建大组件）、状态驱动 UI（UI 随状态变化自动更新）。","tags":["Jetpack Compose","基础"]}
                            ]"""
                            repository.importFromJson(sampleJson, bankId)
                        }
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("导入题库")
                }
                OutlinedButton(
                    onClick = { /* TODO: 模板 */ },
                    modifier = Modifier.weight(1f),
                ) {
                    Text("题库模板")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (questionBanks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.LibraryBooks,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "暂无题库",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "点击上方「导入题库」按钮\n加载内置示例题库",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(questionBanks, key = { it.id }) { bank ->
                        QuestionBankCard(
                            questionBank = bank,
                            onClick = { onNavigateToDetail(bank.id, bank.name) },
                            onDelete = {
                                scope.launch {
                                    repository.deleteQuestionBank(bank.id)
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuestionBankCard(
    questionBank: QuestionBank,
    onClick: () -> Unit,
    onDelete: () -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = questionBank.name + if (questionBank.isDefault) "（默认）" else "",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${questionBank.questionCount} 题",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "更多")
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                ) {
                    DropdownMenuItem(
                        text = { Text("编辑") },
                        onClick = { showMenu = false },
                    )
                    DropdownMenuItem(
                        text = { Text("删除") },
                        onClick = {
                            showMenu = false
                            onDelete()
                        },
                    )
                }
            }
        }
    }
}
