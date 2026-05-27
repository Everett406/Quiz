package com.at210co60.tiku.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "answer_records",
    foreignKeys = [
        ForeignKey(
            entity = QuestionEntity::class,
            parentColumns = ["id"],
            childColumns = ["questionId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = QuestionBankEntity::class,
            parentColumns = ["id"],
            childColumns = ["questionBankId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("questionId"), Index("questionBankId")],
)
data class AnswerRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val questionId: Long,
    val questionBankId: Long,
    val userAnswer: String,
    val isCorrect: Boolean,
    val answeredAt: Long = System.currentTimeMillis(),
    val practiceMode: String, // "sequential", "random", "exam", "wrong"
)
