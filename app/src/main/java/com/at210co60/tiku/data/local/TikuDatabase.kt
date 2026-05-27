package com.at210co60.tiku.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.at210co60.tiku.data.local.dao.QuestionDao
import com.at210co60.tiku.data.local.entity.QuestionEntity

@Database(entities = [QuestionEntity::class], version = 1, exportSchema = false)
abstract class TikuDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao

    companion object {
        @Volatile
        private var INSTANCE: TikuDatabase? = null

        fun getDatabase(context: Context): TikuDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TikuDatabase::class.java,
                    "tiku_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
