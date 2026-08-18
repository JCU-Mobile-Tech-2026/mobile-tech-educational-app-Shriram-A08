package com.example.brainquest.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_attempts")
data class AttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val score: Int,
    val totalQuestions: Int,
    val difficulty: String,
    val completedAt: Long
)
