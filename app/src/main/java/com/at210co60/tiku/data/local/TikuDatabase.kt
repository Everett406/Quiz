package com.at210co60.tiku.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.at210co60.tiku.data.local.dao.AnswerRecordDao
import com.at210co60.tiku.data.local.dao.QuestionBankDao
import com.at210co60.tiku.data.local.dao.QuestionDao
import com.at210co60.tiku.data.local.entity.AnswerRecordEntity
import com.at210co60.tiku.data.local.entity.QuestionBankEntity
import com.at210co60.tiku.data.local.entity.QuestionEntity

@Database(
    entities = [
        QuestionEntity::class,
        QuestionBankEntity::class,
        AnswerRecordEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
abstract class TikuDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun questionBankDao(): QuestionBankDao
    abstract fun answerRecordDao(): AnswerRecordDao

    companion object {
        @Volatile
        private var INSTANCE: TikuDatabase? = null

        fun getDatabase(context: Context): TikuDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TikuDatabase::class.java,
                    "tiku_database",
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
