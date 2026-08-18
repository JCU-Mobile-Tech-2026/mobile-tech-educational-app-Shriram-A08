package com.example.brainquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.brainquest.ui.BrainQuestApp
import com.example.brainquest.ui.quiz.QuizViewModel
import com.example.brainquest.ui.settings.SettingsViewModel
import com.example.brainquest.ui.stats.StatsViewModel
import com.example.brainquest.ui.theme.BrainQuestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val quizViewModel: QuizViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val statsViewModel: StatsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val settings by settingsViewModel.settings.collectAsStateWithLifecycle()

            BrainQuestTheme {
                BrainQuestApp(
                    quizViewModel = quizViewModel,
                    settingsViewModel = settingsViewModel,
                    statsViewModel = statsViewModel,
                    settings = settings
                )
            }
        }
    }
}
