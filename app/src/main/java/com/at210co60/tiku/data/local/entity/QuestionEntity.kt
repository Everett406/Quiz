package com.at210co60.tiku.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "questions",
    foreignKeys = [
        ForeignKey(
            entity = QuestionBankEntity::class,
            parentColumns = ["id"],
            childColumns = ["questionBankId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("questionBankId")],
)
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val questionBankId: Long,
    val title: String,
    val type: String, // "SINGLE_CHOICE", "MULTI_CHOICE", "TRUE_FALSE", "SHORT_ANSWER"
    val options: String, // JSON array string
    val answer: String,
    val explanation: String = "",
    val tags: String = "", // JSON array string
)
