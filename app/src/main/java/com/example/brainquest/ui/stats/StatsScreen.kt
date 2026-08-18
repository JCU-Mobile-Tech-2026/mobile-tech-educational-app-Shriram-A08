package com.example.brainquest.ui.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.brainquest.domain.QuizAttempt
import com.example.brainquest.domain.StatsCalculator
import com.example.brainquest.ui.components.PageColumn
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun StatsScreen(attempts: List<QuizAttempt>) {
    PageColumn {
        Text(
            text = "Learning Statistics",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text("Your progress is calculated from quiz attempts stored in the local Room database.")

        if (attempts.isEmpty()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "No statistics yet",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text("Complete a quiz and your score will appear here.")
                }
            }
        } else {
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(18.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SummaryMetric("Attempts", attempts.size.toString())
                    SummaryMetric(
                        "Average",
                        "${StatsCalculator.averagePercentage(attempts)}%"
                    )
                    SummaryMetric(
                        "Best",
                        "${StatsCalculator.bestPercentage(attempts)}%"
                    )
                }
            }

            Text(
                text = "Recent attempts",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            attempts.take(10).forEach { attempt ->
                AttemptCard(attempt)
            }
        }
    }
}

@Composable
private fun SummaryMetric(label: String, value: String) {
    Column {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun AttemptCard(attempt: QuizAttempt) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${attempt.score}/${attempt.totalQuestions} (${attempt.percentage}%)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(attempt.difficulty.replaceFirstChar { it.uppercase() })
            }
            LinearProgressIndicator(
                progress = { attempt.percentage / 100f },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = formatDate(attempt.completedAt),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

private fun formatDate(epochMillis: Long): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, h:mm a")
    return Instant.ofEpochMilli(epochMillis)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}
