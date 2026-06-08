package com.at210co60.tiku.ui.screen.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.at210co60.tiku.data.repository.FontSize
import com.at210co60.tiku.data.repository.QuestionRepository
import com.at210co60.tiku.data.repository.SettingsRepository
import com.at210co60.tiku.data.repository.ThemeMode
import com.at210co60.tiku.ui.components.WarmButton
import com.at210co60.tiku.ui.components.WarmSecondaryButton
import com.at210co60.tiku.ui.components.WarmTopBar
import com.at210co60.tiku.ui.theme.AccentError
import com.at210co60.tiku.ui.theme.AccentPrimary
import com.at210co60.tiku.ui.theme.Radius
import com.at210co60.tiku.ui.theme.Spacing
import com.at210co60.tiku.ui.theme.Surface
import com.at210co60.tiku.ui.theme.TextPrimary
import com.at210co60.tiku.ui.theme.TextSecondary
import com.at210co60.tiku.ui.theme.WarmCream
import com.at210co60.tiku.ui.theme.WarmWhite
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsRepository: SettingsRepository,
    questionRepository: QuestionRepository,
    onBack: () -> Unit,
    viewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModel.factory(settingsRepository)
    ),
) {
    val settings by viewModel.settings.collectAsState()
    val scope = rememberCoroutineScope()
    var showClearDataDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            WarmTopBar(
                title = "设置",
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
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            Spacer(modifier = Modifier.height(Spacing.sm))

            // Appearance Section
            SettingsSection(title = "外观") {
                SettingItem(
                    label = "主题",
                    options = listOf(
                        "跟随" to ThemeMode.SYSTEM,
                        "亮色" to ThemeMode.LIGHT,
                        "暗色" to ThemeMode.DARK,
                    ),
                    selectedValue = settings.themeMode,
                    onSelect = { viewModel.setThemeMode(it) },
                    valueToLabel = { it.first },
                    valueToCompare = { it.second },
                )
                Spacer(modifier = Modifier.height(Spacing.md))
                SettingItem(
                    label = "字号",
                    options = listOf(
                        "小" to FontSize.SMALL,
                        "标准" to FontSize.NORMAL,
                        "大" to FontSize.LARGE,
                        "特大" to FontSize.EXTRA_LARGE,
                    ),
                    selectedValue = settings.fontSize,
                    onSelect = { viewModel.setFontSize(it) },
                    valueToLabel = { it.first },
                    valueToCompare = { it.second },
                )
            }

            // Data Management Section
            SettingsSection(title = "数据管理") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(Radius.md))
                        .background(AccentError.copy(alpha = 0.08f))
                        .clickable { showClearDataDialog = true }
                        .padding(vertical = Spacing.md),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "清除所有数据",
                        color = AccentError,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }

            // About Section
            SettingsSection(title = "关于") {
                Text(
                    text = "Tiku 题库",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                )
                Spacer(modifier = Modifier.height(Spacing.xs))
                Text(
                    text = "版本 v1.3.0",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                )
                Spacer(modifier = Modifier.height(Spacing.sm))
                Text(
                    text = "一款面向学生的 Android 刷题应用，支持在线刷题、模拟考试、错题记录与学习统计。",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                )
            }

            Spacer(modifier = Modifier.height(Spacing.xl))
        }
    }

    if (showClearDataDialog) {
        AlertDialog(
            onDismissRequest = { showClearDataDialog = false },
            title = { Text("确认清除所有数据？") },
            text = { Text("此操作将删除所有题库、题目和答题记录，且无法恢复。") },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            questionRepository.clearAllData()
                        }
                        showClearDataDialog = false
                    },
                ) {
                    Text("确认清除", color = AccentError)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDataDialog = false }) {
                    Text("取消")
                }
            },
        )
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Radius.md))
            .border(BorderStroke(1.dp, WarmCream), RoundedCornerShape(Radius.md))
            .padding(Spacing.md),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary,
        )
        Spacer(modifier = Modifier.height(Spacing.md))
        content()
    }
}

@Composable
private fun <T> SettingItem(
    label: String,
    options: List<Pair<String, T>>,
    selectedValue: T,
    onSelect: (T) -> Unit,
    valueToLabel: (Pair<String, T>) -> String,
    valueToCompare: (Pair<String, T>) -> T,
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = TextSecondary,
        )
        Spacer(modifier = Modifier.height(Spacing.sm))
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            options.forEach { option ->
                SelectableChip(
                    text = valueToLabel(option),
                    isSelected = valueToCompare(option) == selectedValue,
                    onClick = { onSelect(valueToCompare(option)) },
                )
            }
        }
    }
}

@Composable
private fun SelectableChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val backgroundColor = if (isSelected) AccentPrimary else WarmCream
    val textColor = if (isSelected) Color.White else TextPrimary

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(Radius.sm))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = Spacing.md, vertical = Spacing.sm),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
        )
    }
}
