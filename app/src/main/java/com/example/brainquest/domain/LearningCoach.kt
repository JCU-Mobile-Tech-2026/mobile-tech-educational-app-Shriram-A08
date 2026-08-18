package com.example.brainquest.domain

object LearningCoach {
    fun message(percentage: Int): String = when {
        percentage >= 85 -> "Excellent result. Try Hard difficulty for a bigger challenge."
        percentage >= 65 -> "Good progress. Review the missed questions and try another round."
        percentage >= 50 -> "You are building confidence. Repeat this difficulty to strengthen recall."
        else -> "Start with Easy difficulty and focus on one question at a time."
    }
}
