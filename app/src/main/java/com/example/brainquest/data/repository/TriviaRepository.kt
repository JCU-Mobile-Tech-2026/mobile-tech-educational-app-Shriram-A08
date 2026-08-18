package com.example.brainquest.data.repository

import com.example.brainquest.data.remote.TriviaApi
import com.example.brainquest.domain.QuizQuestion
import javax.inject.Inject

interface TriviaRepository {
    suspend fun getQuestions(amount: Int, difficulty: String): Result<List<QuizQuestion>>
}

class TriviaRepositoryImpl @Inject constructor(
    private val api: TriviaApi
) : TriviaRepository {

    override suspend fun getQuestions(
        amount: Int,
        difficulty: String
    ): Result<List<QuizQuestion>> = runCatching {
        // The external service supplies fresh computer-science questions for each quiz.
        val response = api.getQuestions(
            amount = amount,
            difficulty = difficulty
        )

        if (response.responseCode != 0 || response.results.isEmpty()) {
            error("The quiz service returned no questions.")
        }

        response.results.map { dto ->
            // The API can return HTML entities. Decode the common ones before showing text.
            val correct = decodeTriviaText(dto.correctAnswer)
            val options = (dto.incorrectAnswers.map(::decodeTriviaText) + correct).shuffled()

            QuizQuestion(
                question = decodeTriviaText(dto.question),
                correctAnswer = correct,
                options = options
            )
        }
    }

    private fun decodeTriviaText(text: String): String = text
        .replace("&quot;", "\"")
        .replace("&#039;", "'")
        .replace("&amp;", "&")
        .replace("&lt;", "<")
        .replace("&gt;", ">")
        .replace("&eacute;", "é")
}
