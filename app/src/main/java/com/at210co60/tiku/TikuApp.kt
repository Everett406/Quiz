package com.at210co60.tiku

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.at210co60.tiku.data.local.TikuDatabase
import com.at210co60.tiku.data.repository.QuestionRepository
import com.at210co60.tiku.navigation.Screen
import com.at210co60.tiku.ui.screen.detail.QuizDetailScreen
import com.at210co60.tiku.ui.screen.home.HomeScreen
import com.at210co60.tiku.ui.screen.quiz.QuizPracticeScreen
import com.at210co60.tiku.ui.screen.settings.SettingsScreen
import com.at210co60.tiku.ui.screen.wrong.WrongQuestionsScreen
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
                    onNavigateToDetail = { title ->
                        navController.navigate("quiz_detail/$title")
                    },
                    onNavigateToSettings = {
                        navController.navigate(Screen.Settings.route)
                    },
                    onNavigateToImport = {
                        navController.navigate(Screen.Home.route) // TODO
                    },
                )
            }
            composable(
                route = "quiz_detail/{title}",
                arguments = listOf(navArgument("title") { type = NavType.StringType }),
            ) { backStackEntry ->
                val title = backStackEntry.arguments?.getString("title") ?: "题库"
                QuizDetailScreen(
                    title = title,
                    onBack = { navController.popBackStack() },
                    onNavigateToPractice = { mode ->
                        if (mode == "wrong") {
                            navController.navigate(Screen.WrongQuestions.route)
                        } else {
                            navController.navigate(Screen.QuizPractice.createRoute(mode))
                        }
                    },
                )
            }
            composable(
                route = Screen.QuizPractice.route,
                arguments = listOf(navArgument("mode") { type = NavType.StringType }),
            ) { backStackEntry ->
                val mode = backStackEntry.arguments?.getString("mode") ?: "sequential"
                QuizPracticeScreen(
                    repository = repository,
                    mode = mode,
                    onBack = { navController.popBackStack() },
                )
            }
            composable(Screen.WrongQuestions.route) {
                WrongQuestionsScreen(
                    onBack = { navController.popBackStack() },
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen(
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}
