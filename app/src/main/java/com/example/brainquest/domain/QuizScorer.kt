package com.example.brainquest.domain

object QuizScorer {
    fun isCorrect(selectedAnswer: String, correctAnswer: String): Boolean =
        selectedAnswer.trim().equals(correctAnswer.trim(), ignoreCase = true)

    fun percentage(score: Int, total: Int): Int {
        if (total <= 0) return 0
        return ((score.toDouble() / total.toDouble()) * 100).toInt()
    }
}
