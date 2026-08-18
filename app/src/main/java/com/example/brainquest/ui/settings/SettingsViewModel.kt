package com.example.brainquest.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brainquest.data.repository.SettingsRepository
import com.example.brainquest.domain.AppSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
) : ViewModel() {

    val settings: StateFlow<AppSettings> = repository.settings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AppSettings()
    )

    fun setDifficulty(value: String) {
        viewModelScope.launch { repository.setDifficulty(value) }
    }

    fun setQuestionCount(value: Int) {
        viewModelScope.launch { repository.setQuestionCount(value) }
    }

    fun setShowAnswerFeedback(value: Boolean) {
        viewModelScope.launch { repository.setShowAnswerFeedback(value) }
    }
}
