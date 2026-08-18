package com.example.brainquest.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.brainquest.domain.QuizAttempt
import com.example.brainquest.domain.StatsCalculator
import com.example.brainquest.ui.components.PageColumn

@Composable
fun LandingScreen(
    attempts: List<QuizAttempt>,
    onStartQuiz: () -> Unit,
    onOpenStats: () -> Unit,
    onOpenSettings: () -> Unit
) {
    PageColumn {
        Text(
            text = "BrainQuest",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Computer Science & IT practice for university learners",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Build recall and problem-solving skills with short multiple-choice quiz sessions.",
            style = MaterialTheme.typography.bodyLarge
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Your progress",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                if (attempts.isEmpty()) {
                    Text("No completed quizzes yet. Start your first quiz to create learning statistics.")
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Metric(label = "Attempts", value = attempts.size.toString())
                        Metric(
                            label = "Average",
                            value = "${StatsCalculator.averagePercentage(attempts)}%"
                        )
                        Metric(
                            label = "Best",
                            value = "${StatsCalculator.bestPercentage(attempts)}%"
                        )
                    }
                }
            }
        }

        Button(
            onClick = onStartQuiz,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Quiz")
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onOpenStats,
                modifier = Modifier.weight(1f)
            ) {
                Text("View Statistics")
            }
            OutlinedButton(
                onClick = onOpenSettings,
                modifier = Modifier.weight(1f)
            ) {
                Text("Settings")
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Privacy & transparency",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "BrainQuest does not require an account, advertising ID, contacts, location, camera, or microphone. " +
                        "Quiz history stays on this device. When online questions are requested, the selected quiz settings " +
                        "and normal network metadata may be visible to the external question service."
                )
            }
        }
    }
}

@Composable
private fun Metric(label: String, value: String) {
    Column {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
