package com.example.brainquest.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.brainquest.domain.AppSettings
import com.example.brainquest.ui.home.LandingScreen
import com.example.brainquest.ui.quiz.QuizScreen
import com.example.brainquest.ui.quiz.QuizViewModel
import com.example.brainquest.ui.settings.SettingsScreen
import com.example.brainquest.ui.settings.SettingsViewModel
import com.example.brainquest.ui.stats.StatsScreen
import com.example.brainquest.ui.stats.StatsViewModel

private enum class Destination(
    val route: String,
    val label: String,
    val symbol: String
) {
    Home("home", "Home", "⌂"),
    Quiz("quiz", "Quiz", "Q"),
    Stats("stats", "Stats", "▥"),
    Settings("settings", "Settings", "⚙")
}

@Composable
fun BrainQuestApp(
    quizViewModel: QuizViewModel,
    settingsViewModel: SettingsViewModel,
    statsViewModel: StatsViewModel,
    settings: AppSettings
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val attempts by statsViewModel.attempts.collectAsStateWithLifecycle()
    val quizState by quizViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            NavigationBar {
                Destination.entries.forEach { destination ->
                    val selected = currentDestination?.hierarchy?.any {
                        it.route == destination.route
                    } == true

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(Destination.Home.route) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Text(destination.symbol) },
                        label = { Text(destination.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destination.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Destination.Home.route) {
                LandingScreen(
                    attempts = attempts,
                    onStartQuiz = {
                        quizViewModel.startQuiz(
                            amount = settings.questionCount,
                            difficulty = settings.difficulty
                        )
                        navController.navigate(Destination.Quiz.route)
                    },
                    onOpenStats = { navController.navigate(Destination.Stats.route) },
                    onOpenSettings = { navController.navigate(Destination.Settings.route) }
                )
            }

            composable(Destination.Quiz.route) {
                QuizScreen(
                    state = quizState,
                    settings = settings,
                    onStart = {
                        quizViewModel.startQuiz(
                            amount = settings.questionCount,
                            difficulty = settings.difficulty
                        )
                    },
                    onRetryOnline = {
                        quizViewModel.startQuiz(
                            amount = settings.questionCount,
                            difficulty = settings.difficulty
                        )
                    },
                    onUseOffline = {
                        quizViewModel.useOfflineQuestions(settings.difficulty)
                    },
                    onAnswerSelected = quizViewModel::selectAnswer,
                    onSubmit = {
                        quizViewModel.submitAnswer(settings.showAnswerFeedback)
                    },
                    onRestart = {
                        quizViewModel.startQuiz(
                            amount = settings.questionCount,
                            difficulty = settings.difficulty
                        )
                    },
                    onViewStats = { navController.navigate(Destination.Stats.route) }
                )
            }

            composable(Destination.Stats.route) {
                StatsScreen(attempts = attempts)
            }

            composable(Destination.Settings.route) {
                SettingsScreen(
                    settings = settings,
                    onDifficultyChanged = settingsViewModel::setDifficulty,
                    onQuestionCountChanged = settingsViewModel::setQuestionCount,
                    onFeedbackChanged = settingsViewModel::setShowAnswerFeedback,
                    onClearHistory = statsViewModel::clearHistory
                )
            }
        }
    }
}
