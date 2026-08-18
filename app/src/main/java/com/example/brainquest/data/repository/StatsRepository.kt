package com.example.brainquest.data.repository

import com.example.brainquest.data.local.AttemptDao
import com.example.brainquest.data.local.AttemptEntity
import com.example.brainquest.domain.QuizAttempt
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface StatsRepository {
    fun observeAttempts(): Flow<List<QuizAttempt>>
    suspend fun saveAttempt(score: Int, totalQuestions: Int, difficulty: String)
    suspend fun clearAttempts()
}

class StatsRepositoryImpl @Inject constructor(
    private val dao: AttemptDao
) : StatsRepository {

    override fun observeAttempts(): Flow<List<QuizAttempt>> =
        dao.observeAttempts().map { entities ->
            entities.map { entity ->
                entity.toDomain()
            }
        }

    override suspend fun saveAttempt(
        score: Int,
        totalQuestions: Int,
        difficulty: String
    ) {
        dao.insertAttempt(
            AttemptEntity(
                score = score,
                totalQuestions = totalQuestions,
                difficulty = difficulty,
                completedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun clearAttempts() {
        dao.clearAttempts()
    }

    private fun AttemptEntity.toDomain() = QuizAttempt(
        id = id,
        score = score,
        totalQuestions = totalQuestions,
        difficulty = difficulty,
        completedAt = completedAt
    )
}