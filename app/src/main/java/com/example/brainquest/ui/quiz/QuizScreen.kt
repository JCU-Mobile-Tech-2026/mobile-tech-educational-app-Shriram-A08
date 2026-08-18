package com.example.brainquest.ui.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.brainquest.domain.AppSettings
import com.example.brainquest.domain.LearningCoach
import com.example.brainquest.domain.QuizScorer

@Composable
fun QuizScreen(
    state: QuizUiState,
    settings: AppSettings,
    onStart: () -> Unit,
    onRetryOnline: () -> Unit,
    onUseOffline: () -> Unit,
    onAnswerSelected: (String) -> Unit,
    onSubmit: () -> Unit,
    onRestart: () -> Unit,
    onViewStats: () -> Unit
) {
    when {
        state.isLoading -> LoadingState()
        state.errorMessage != null -> ErrorState(
            message = state.errorMessage,
            onRetryOnline = onRetryOnline,
            onUseOffline = onUseOffline
        )
        state.isFinished -> FinishedState(
            score = state.score,
            total = state.questions.size,
            difficulty = state.difficulty,
            offline = state.isOfflineMode,
            onRestart = onRestart,
            onViewStats = onViewStats
        )
        state.questions.isEmpty() -> ReadyState(
            settings = settings,
            onStart = onStart
        )
        else -> ActiveQuiz(
            state = state,
            showAnswerFeedback = settings.showAnswerFeedback,
            onAnswerSelected = onAnswerSelected,
            onSubmit = onSubmit
        )
    }
}

@Composable
private fun ReadyState(
    settings: AppSettings,
    onStart: () -> Unit
) {
    CenteredQuizColumn {
        Text(
            text = "Quiz Activity",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text("Questions are loaded from an online computer-science quiz service.")
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Difficulty: ${settings.difficulty.replaceFirstChar { it.uppercase() }}")
                Text("Questions: ${settings.questionCount}")
                Text(
                    if (settings.showAnswerFeedback) {
                        "Answer feedback: shown after each answer"
                    } else {
                        "Answer feedback: hidden until the quiz finishes"
                    }
                )
            }
        }
        Button(onClick = onStart, modifier = Modifier.fillMaxWidth()) {
            Text("Load Questions & Start")
        }
    }
}

@Composable
private fun LoadingState() {
    CenteredQuizColumn(horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator()
        Text("Loading online questions…")
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetryOnline: () -> Unit,
    onUseOffline: () -> Unit
) {
    CenteredQuizColumn {
        Text(
            text = "Could not load the quiz",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(message)
        Button(onClick = onRetryOnline, modifier = Modifier.fillMaxWidth()) {
            Text("Retry Online")
        }
        OutlinedButton(onClick = onUseOffline, modifier = Modifier.fillMaxWidth()) {
            Text("Use Offline Sample")
        }
    }
}

@Composable
private fun ActiveQuiz(
    state: QuizUiState,
    showAnswerFeedback: Boolean,
    onAnswerSelected: (String) -> Unit,
    onSubmit: () -> Unit
) {
    val question = state.currentQuestion ?: return
    val progress = (state.currentIndex + 1).toFloat() / state.questions.size.toFloat()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 760.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Question ${state.currentIndex + 1} of ${state.questions.size}",
                        fontWeight = FontWeight.SemiBold
                    )
                    Text("Score: ${state.score}")
                }
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth()
                )
                if (state.isOfflineMode) {
                    Text(
                        text = "Offline sample mode",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                Text(
                    text = question.question,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        items(question.options) { option ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 760.dp),
                onClick = { onAnswerSelected(option) },
                enabled = !state.answerChecked
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = state.selectedAnswer == option,
                        onClick = { onAnswerSelected(option) },
                        enabled = !state.answerChecked
                    )
                    Text(
                        text = option,
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        if (showAnswerFeedback && state.answerChecked) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 760.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = if (state.lastAnswerCorrect == true) "Correct" else "Not quite",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (state.lastAnswerCorrect == false) {
                            Text("Correct answer: ${question.correctAnswer}")
                        }
                        Text("Use the feedback to reinforce recall before moving on.")
                    }
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 760.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                HorizontalDivider()
                Button(
                    onClick = onSubmit,
                    enabled = state.selectedAnswer != null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val isLast = state.currentIndex == state.questions.lastIndex
                    val label = when {
                        showAnswerFeedback && !state.answerChecked -> "Check Answer"
                        isLast -> "Finish Quiz"
                        else -> "Next Question"
                    }
                    Text(label)
                }
            }
        }
    }
}

@Composable
private fun FinishedState(
    score: Int,
    total: Int,
    difficulty: String,
    offline: Boolean,
    onRestart: () -> Unit,
    onViewStats: () -> Unit
) {
    val percentage = QuizScorer.percentage(score, total)

    CenteredQuizColumn {
        Text(
            text = "Quiz Complete",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "$score / $total",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold
                )
                Text("Score: $percentage%")
                Text("Difficulty: ${difficulty.replaceFirstChar { it.uppercase() }}")
                if (offline) Text("This attempt used the offline sample question set.")
                Text(LearningCoach.message(percentage))
            }
        }
        Button(onClick = onRestart, modifier = Modifier.fillMaxWidth()) {
            Text("Try Another Quiz")
        }
        OutlinedButton(onClick = onViewStats, modifier = Modifier.fillMaxWidth()) {
            Text("View Statistics")
        }
    }
}

@Composable
private fun CenteredQuizColumn(
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 760.dp),
            horizontalAlignment = horizontalAlignment,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            content = content
        )
    }
}
