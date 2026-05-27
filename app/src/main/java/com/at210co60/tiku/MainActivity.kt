package com.at210co60.tiku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.at210co60.tiku.data.local.TikuDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val database = TikuDatabase.getDatabase(applicationContext)
        setContent {
            TikuApp(database = database)
        }
    }
}
