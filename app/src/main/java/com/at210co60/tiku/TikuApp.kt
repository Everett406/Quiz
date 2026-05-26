package com.at210co60.tiku

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.at210co60.tiku.navigation.Screen
import com.at210co60.tiku.ui.screen.home.HomeScreen
import com.at210co60.tiku.ui.screen.question.QuestionListScreen
import com.at210co60.tiku.ui.theme.TikuTheme

@Composable
fun TikuApp() {
    TikuTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToQuestionList = {
                        navController.navigate(Screen.QuestionList.route)
                    },
                )
            }
            composable(Screen.QuestionList.route) {
                QuestionListScreen(
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}
