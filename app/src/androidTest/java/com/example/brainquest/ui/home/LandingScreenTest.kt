package com.example.brainquest.ui.home

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class LandingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun startQuizButton_isVisibleAndClickable() {
        var clicked = false

        composeTestRule.setContent {
            MaterialTheme {
                LandingScreen(
                    attempts = emptyList(),
                    onStartQuiz = { clicked = true },
                    onOpenStats = {},
                    onOpenSettings = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Start Quiz")
            .assertIsDisplayed()
            .performClick()

        assertTrue(clicked)
    }
}
