package com.at210co60.tiku.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object QuizDetail : Screen("quiz_detail/{bankId}/{bankName}") {
        fun createRoute(bankId: Long, bankName: String) = "quiz_detail/$bankId/$bankName"
    }
    data object QuizPractice : Screen("quiz_practice/{mode}/{bankId}") {
        fun createRoute(mode: String, bankId: Long) = "quiz_practice/$mode/$bankId"
    }
    data object WrongQuestions : Screen("wrong_questions")
    data object Settings : Screen("settings")
}
