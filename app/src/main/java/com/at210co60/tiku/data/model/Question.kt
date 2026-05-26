package com.at210co60.tiku.data.model

data class Question(
    val id: Long,
    val title: String,
    val type: QuestionType,
    val options: List<String>,
    val answer: String,
    val tags: List<String> = emptyList(),
)

enum class QuestionType {
    SINGLE_CHOICE,
    MULTI_CHOICE,
    TRUE_FALSE,
    SHORT_ANSWER,
}
