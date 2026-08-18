package com.example.brainquest.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.brainquest.domain.AppSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.brainQuestDataStore by preferencesDataStore(name = "brainquest_settings")

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val difficulty = stringPreferencesKey("difficulty")
        val questionCount = intPreferencesKey("question_count")
        val showAnswerFeedback = booleanPreferencesKey("show_answer_feedback")
    }

    val settings: Flow<AppSettings> = context.brainQuestDataStore.data.map { preferences ->
        AppSettings(
            difficulty = preferences[Keys.difficulty] ?: "medium",
            questionCount = preferences[Keys.questionCount] ?: 10,
            showAnswerFeedback = preferences[Keys.showAnswerFeedback] ?: true
        )
    }

    suspend fun setDifficulty(value: String) {
        context.brainQuestDataStore.edit { it[Keys.difficulty] = value }
    }

    suspend fun setQuestionCount(value: Int) {
        context.brainQuestDataStore.edit { it[Keys.questionCount] = value.coerceIn(5, 15) }
    }

    suspend fun setShowAnswerFeedback(value: Boolean) {
        context.brainQuestDataStore.edit { it[Keys.showAnswerFeedback] = value }
    }
}
