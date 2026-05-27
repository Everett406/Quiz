package com.at210co60.tiku.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK,
}

enum class FontSize {
    SMALL,
    NORMAL,
    LARGE,
    EXTRA_LARGE,
}

data class UserSettings(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val fontSize: FontSize = FontSize.NORMAL,
)

class SettingsRepository(private val context: Context) {
    private object PreferencesKeys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val FONT_SIZE = stringPreferencesKey("font_size")
    }

    val settings: Flow<UserSettings> = context.dataStore.data.map { preferences ->
        UserSettings(
            themeMode = preferences[PreferencesKeys.THEME_MODE]?.let {
                ThemeMode.valueOf(it)
            } ?: ThemeMode.SYSTEM,
            fontSize = preferences[PreferencesKeys.FONT_SIZE]?.let {
                FontSize.valueOf(it)
            } ?: FontSize.NORMAL,
        )
    }

    val themeMode: Flow<ThemeMode> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.THEME_MODE]?.let {
            ThemeMode.valueOf(it)
        } ?: ThemeMode.SYSTEM
    }

    val fontSize: Flow<FontSize> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.FONT_SIZE]?.let {
            FontSize.valueOf(it)
        } ?: FontSize.NORMAL
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = mode.name
        }
    }

    suspend fun setFontSize(size: FontSize) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.FONT_SIZE] = size.name
        }
    }
}
