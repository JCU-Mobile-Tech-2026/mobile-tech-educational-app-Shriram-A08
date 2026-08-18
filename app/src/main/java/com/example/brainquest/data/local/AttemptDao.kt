package com.example.brainquest.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AttemptDao {
    @Query("SELECT * FROM quiz_attempts ORDER BY completedAt DESC")
    fun observeAttempts(): Flow<List<AttemptEntity>>

    @Insert
    suspend fun insertAttempt(attempt: AttemptEntity)

    @Query("DELETE FROM quiz_attempts")
    suspend fun clearAttempts()
}
