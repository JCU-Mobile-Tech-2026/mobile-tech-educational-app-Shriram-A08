package com.example.brainquest.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class QuizScorerTest {

    @Test
    fun correctAnswer_isRecognisedIgnoringCaseAndSpaces() {
        assertTrue(QuizScorer.isCorrect(" queue ", "Queue"))
    }

    @Test
    fun incorrectAnswer_returnsFalse() {
        assertFalse(QuizScorer.isCorrect("Stack", "Queue"))
    }

    @Test
    fun percentage_handlesNormalAndEmptyTotals() {
        assertEquals(80, QuizScorer.percentage(8, 10))
        assertEquals(0, QuizScorer.percentage(2, 0))
    }
}
