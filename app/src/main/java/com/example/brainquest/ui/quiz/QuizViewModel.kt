package com.example.brainquest.ui.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brainquest.data.repository.StatsRepository
import com.example.brainquest.data.repository.TriviaRepository
import com.example.brainquest.domain.OfflineQuestionBank
import com.example.brainquest.domain.QuizQuestion
import com.example.brainquest.domain.QuizScorer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class QuizUiState(
    val questions: List<QuizQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedAnswer: String? = null,
    val score: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isFinished: Boolean = false,
    val isOfflineMode: Boolean = false,
    val answerChecked: Boolean = false,
    val lastAnswerCorrect: Boolean? = null,
    val difficulty: String = "medium"
) {
    val currentQuestion: QuizQuestion?
        get() = questions.getOrNull(currentIndex)
}

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val triviaRepository: TriviaRepository,
    private val statsRepository: StatsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    fun startQuiz(amount: Int, difficulty: String) {
        viewModelScope.launch {
            _uiState.value = QuizUiState(
                isLoading = true,
                difficulty = difficulty
            )

            // Keep networking outside the UI layer; the repository owns the API call.
            triviaRepository.getQuestions(amount, difficulty)
                .onSuccess { questions ->
                    _uiState.value = QuizUiState(
                        questions = questions,
                        difficulty = difficulty
                    )
                }
                .onFailure {
                    _uiState.value = QuizUiState(
                        errorMessage = "Could not load online questions. Check your internet connection or use the offline sample.",
                        difficulty = difficulty
                    )
                }
        }
    }

    fun useOfflineQuestions(difficulty: String) {
        _uiState.value = QuizUiState(
            questions = OfflineQuestionBank.questions.shuffled(),
            difficulty = difficulty,
            isOfflineMode = true
        )
    }

    fun selectAnswer(answer: String) {
        val state = _uiState.value
        if (state.isFinished || state.answerChecked) return
        _uiState.update { it.copy(selectedAnswer = answer) }
    }

    fun submitAnswer(showFeedback: Boolean) {
        val state = _uiState.value
        val currentQuestion = state.currentQuestion ?: return
        val selected = state.selectedAnswer ?: return

        if (showFeedback && state.answerChecked) {
            moveToNextOrFinish()
            return
        }

        val correct = QuizScorer.isCorrect(selected, currentQuestion.correctAnswer)
        val updatedScore = state.score + if (correct) 1 else 0

        if (showFeedback) {
            _uiState.update {
                it.copy(
                    score = updatedScore,
                    answerChecked = true,
                    lastAnswerCorrect = correct
                )
            }
        } else {
            moveToNextOrFinish(updatedScore)
        }
    }

    fun resetQuiz() {
        _uiState.value = QuizUiState()
    }

    private fun moveToNextOrFinish(scoreOverride: Int? = null) {
        val state = _uiState.value
        val finalScore = scoreOverride ?: state.score
        val isLastQuestion = state.currentIndex == state.questions.lastIndex

        if (isLastQuestion) {
            _uiState.update {
                it.copy(
                    score = finalScore,
                    isFinished = true,
                    answerChecked = false
                )
            }
            // Persist one summary record per completed quiz so statistics survive restarts.
            saveAttempt(finalScore, state.questions.size, state.difficulty)
        } else {
            _uiState.update {
                it.copy(
                    currentIndex = it.currentIndex + 1,
                    selectedAnswer = null,
                    score = finalScore,
                    answerChecked = false,
                    lastAnswerCorrect = null
                )
            }
        }
    }

    private fun saveAttempt(score: Int, total: Int, difficulty: String) {
        viewModelScope.launch {
            statsRepository.saveAttempt(
                score = score,
                totalQuestions = total,
                difficulty = difficulty
            )
        }
    }
}
