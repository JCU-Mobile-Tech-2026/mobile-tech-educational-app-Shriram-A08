package com.example.brainquest.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class StatsCalculatorTest {

    @Test
    fun averageAndBestPercentage_areCalculatedFromAttempts() {
        val attempts = listOf(
            QuizAttempt(1, 6, 10, "easy", 1L),
            QuizAttempt(2, 8, 10, "medium", 2L),
            QuizAttempt(3, 10, 10, "hard", 3L)
        )

        assertEquals(80, StatsCalculator.averagePercentage(attempts))
        assertEquals(100, StatsCalculator.bestPercentage(attempts))
    }

    @Test
    fun emptyAttempts_returnZero() {
        assertEquals(0, StatsCalculator.averagePercentage(emptyList()))
        assertEquals(0, StatsCalculator.bestPercentage(emptyList()))
    }
}
