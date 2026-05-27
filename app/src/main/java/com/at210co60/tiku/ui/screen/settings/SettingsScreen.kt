package com.at210co60.tiku.ui.screen.settings

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.at210co60.tiku.data.repository.FontSize
import com.at210co60.tiku.data.repository.SettingsRepository
import com.at210co60.tiku.data.repository.ThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsRepository: SettingsRepository,
    onBack: () -> Unit,
    viewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModel.factory(settingsRepository)
    ),
) {
    val settings by viewModel.settings.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("设置") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            SettingCard {
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
                Spacer(modifier = Modifier.height(16.dp))
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
        }
    }
}

@Composable
private fun SettingCard(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
            .padding(16.dp),
    ) {
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
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            options.forEach { option ->
                SegmentedButton(
                    text = valueToLabel(option),
                    isSelected = valueToCompare(option) == selectedValue,
                    onClick = { onSelect(valueToCompare(option)) },
                )
            }
        }
    }
}

@Composable
private fun SegmentedButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.onSurface
    } else {
        Color.Transparent
    }
    val textColor = if (isSelected) {
        MaterialTheme.colorScheme.surface
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    val borderColor = MaterialTheme.colorScheme.outline

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
