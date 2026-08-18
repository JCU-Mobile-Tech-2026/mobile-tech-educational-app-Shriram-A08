package com.example.brainquest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [AttemptEntity::class],
    version = 1,
    exportSchema = true
)
abstract class BrainQuestDatabase : RoomDatabase() {
    abstract fun attemptDao(): AttemptDao
}
