package com.example.brainquest.domain

object StatsCalculator {
    fun averagePercentage(attempts: List<QuizAttempt>): Int {
        if (attempts.isEmpty()) return 0
        return attempts.map { it.percentage }.average().toInt()
    }

    fun bestPercentage(attempts: List<QuizAttempt>): Int =
        attempts.maxOfOrNull { it.percentage } ?: 0
}
