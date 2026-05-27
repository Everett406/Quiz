package com.at210co60.tiku.data.model

data class Question(
    val id: Long = 0,
    val title: String,
    val type: QuestionType,
    val options: List<String> = emptyList(),
    val answer: String,
    val explanation: String = "",
    val tags: List<String> = emptyList(),
)

enum class QuestionType {
    SINGLE_CHOICE,
    MULTI_CHOICE,
    TRUE_FALSE,
    SHORT_ANSWER,
}
