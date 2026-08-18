package com.example.brainquest.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface TriviaApi {
    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int,
        @Query("category") category: Int = 18,
        @Query("difficulty") difficulty: String,
        @Query("type") type: String = "multiple"
    ): TriviaResponseDto
}

data class TriviaResponseDto(
    @SerializedName("response_code") val responseCode: Int,
    val results: List<TriviaQuestionDto>
)

data class TriviaQuestionDto(
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
    @SerializedName("correct_answer") val correctAnswer: String,
    @SerializedName("incorrect_answers") val incorrectAnswers: List<String>
)
