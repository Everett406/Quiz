package com.at210co60.tiku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.at210co60.tiku.data.local.TikuDatabase
import com.at210co60.tiku.data.repository.SettingsRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val database = TikuDatabase.getDatabase(applicationContext)
        val settingsRepository = SettingsRepository(applicationContext)
        setContent {
            TikuApp(
                database = database,
                settingsRepository = settingsRepository,
            )
        }
    }
}
