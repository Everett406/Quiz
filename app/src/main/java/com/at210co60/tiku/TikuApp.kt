package com.at210co60.tiku

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.at210co60.tiku.data.local.TikuDatabase
import com.at210co60.tiku.data.repository.QuestionRepository
import com.at210co60.tiku.navigation.Screen
import com.at210co60.tiku.ui.screen.home.HomeScreen
import com.at210co60.tiku.ui.screen.import.ImportScreen
import com.at210co60.tiku.ui.screen.question.QuestionListScreen
import com.at210co60.tiku.ui.screen.quiz.QuizPracticeScreen
import com.at210co60.tiku.ui.theme.TikuTheme

@Composable
fun TikuApp(database: TikuDatabase) {
    TikuTheme {
        val navController = rememberNavController()
        val repository = remember { QuestionRepository(database.questionDao()) }

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToQuestionList = {
                        navController.navigate(Screen.QuestionList.route)
                    },
                    onNavigateToQuizPractice = {
                        navController.navigate(Screen.QuizPractice.route)
                    },
                    onNavigateToImport = {
                        navController.navigate(Screen.Import.route)
                    },
                )
            }
            composable(Screen.QuestionList.route) {
                QuestionListScreen(
                    repository = repository,
                    onBack = { navController.popBackStack() },
                )
            }
            composable(Screen.QuizPractice.route) {
                QuizPracticeScreen(
                    repository = repository,
                    onBack = { navController.popBackStack() },
                )
            }
            composable(Screen.Import.route) {
                ImportScreen(
                    repository = repository,
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}
