package com.at210co60.tiku.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object QuestionList : Screen("question_list")
}
