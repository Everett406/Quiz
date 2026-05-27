package com.at210co60.tiku.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object QuizDetail : Screen("quiz_detail")
    data object QuizPractice : Screen("quiz_practice/{mode}") {
        fun createRoute(mode: String) = "quiz_practice/$mode"
    }
    data object WrongQuestions : Screen("wrong_questions")
    data object Settings : Screen("settings")
}
