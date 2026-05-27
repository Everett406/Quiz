package com.at210co60.tiku

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.at210co60.tiku.data.local.TikuDatabase
import com.at210co60.tiku.data.repository.QuestionRepository
import com.at210co60.tiku.data.repository.SettingsRepository
import com.at210co60.tiku.data.repository.ThemeMode
import com.at210co60.tiku.navigation.Screen
import com.at210co60.tiku.ui.screen.detail.QuizDetailScreen
import com.at210co60.tiku.ui.screen.home.HomeScreen
import com.at210co60.tiku.ui.screen.quiz.QuizPracticeScreen
import com.at210co60.tiku.ui.screen.settings.SettingsScreen
import com.at210co60.tiku.ui.screen.wrong.WrongQuestionsScreen
import com.at210co60.tiku.ui.theme.TikuTheme

@Composable
fun TikuApp(
    database: TikuDatabase,
    settingsRepository: SettingsRepository,
) {
    val settings by settingsRepository.settings.collectAsState(initial = null)

    val darkTheme = when (settings?.themeMode ?: ThemeMode.SYSTEM) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    TikuTheme(
        darkTheme = darkTheme,
        fontSize = settings?.fontSize ?: com.at210co60.tiku.data.repository.FontSize.NORMAL,
    ) {
        val navController = rememberNavController()
        val repository = remember {
            QuestionRepository(
                database.questionDao(),
                database.questionBankDao(),
                database.answerRecordDao()
            )
        }

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    repository = repository,
                    onNavigateToDetail = { bankId, bankName ->
                        navController.navigate("quiz_detail/$bankId/$bankName")
                    },
                    onNavigateToSettings = {
                        navController.navigate(Screen.Settings.route)
                    },
                )
            }
            composable(
                route = "quiz_detail/{bankId}/{bankName}",
                arguments = listOf(
                    navArgument("bankId") { type = NavType.LongType },
                    navArgument("bankName") { type = NavType.StringType },
                ),
            ) { backStackEntry ->
                val bankId = backStackEntry.arguments?.getLong("bankId") ?: 0L
                val bankName = backStackEntry.arguments?.getString("bankName") ?: "题库"
                QuizDetailScreen(
                    bankId = bankId,
                    title = bankName,
                    repository = repository,
                    onBack = { navController.popBackStack() },
                    onNavigateToPractice = { mode ->
                        if (mode == "wrong") {
                            navController.navigate(Screen.WrongQuestions.route)
                        } else {
                            navController.navigate(Screen.QuizPractice.createRoute(mode, bankId))
                        }
                    },
                )
            }
            composable(
                route = Screen.QuizPractice.route,
                arguments = listOf(
                    navArgument("mode") { type = NavType.StringType },
                    navArgument("bankId") { type = NavType.LongType },
                ),
            ) { backStackEntry ->
                val mode = backStackEntry.arguments?.getString("mode") ?: "sequential"
                val bankId = backStackEntry.arguments?.getLong("bankId") ?: 0L
                QuizPracticeScreen(
                    repository = repository,
                    mode = mode,
                    bankId = bankId,
                    onBack = { navController.popBackStack() },
                )
            }
            composable(Screen.WrongQuestions.route) {
                WrongQuestionsScreen(
                    repository = repository,
                    onBack = { navController.popBackStack() },
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen(
                    settingsRepository = settingsRepository,
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}
