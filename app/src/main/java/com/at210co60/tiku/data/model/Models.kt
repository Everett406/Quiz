package com.at210co60.tiku.data.model

data class QuestionBank(
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val questionCount: Int = 0,
    val isDefault: Boolean = false,
)

data class BankStats(
    val bankId: Long,
    val totalQuestions: Int = 0,
    val answeredQuestions: Int = 0,
    val correctAnswers: Int = 0,
    val wrongAnswers: Int = 0,
) {
    val correctRate: Int
        get() = if (answeredQuestions > 0) (correctAnswers * 100 / answeredQuestions) else 0
}
