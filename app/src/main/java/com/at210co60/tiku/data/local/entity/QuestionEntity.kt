package com.at210co60.tiku.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val type: String, // "SINGLE_CHOICE", "MULTI_CHOICE", "TRUE_FALSE", "SHORT_ANSWER"
    val options: String, // JSON array string: ["A","B","C","D"]
    val answer: String,
    val explanation: String = "",
    val tags: String = "", // JSON array string: ["Kotlin","基础"]
)
