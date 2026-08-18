package com.example.brainquest.domain

data class QuizQuestion(
    val question: String,
    val correctAnswer: String,
    val options: List<String>
)

data class QuizAttempt(
    val id: Long,
    val score: Int,
    val totalQuestions: Int,
    val difficulty: String,
    val completedAt: Long
) {
    val percentage: Int
        get() = if (totalQuestions == 0) 0 else (score * 100) / totalQuestions
}

data class AppSettings(
    val difficulty: String = "medium",
    val questionCount: Int = 10,
    val showAnswerFeedback: Boolean = true
)
