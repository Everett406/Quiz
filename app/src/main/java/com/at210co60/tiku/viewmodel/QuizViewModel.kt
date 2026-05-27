package com.at210co60.tiku.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.at210co60.tiku.data.model.Question
import com.at210co60.tiku.data.model.QuestionType
import com.at210co60.tiku.data.repository.QuestionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class QuizViewModel(
    private val repository: QuestionRepository,
    private val mode: String = "sequential",
    private val bankId: Long = 0,
) : ViewModel() {

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _selectedAnswer = MutableStateFlow<String?>(null)
    val selectedAnswer: StateFlow<String?> = _selectedAnswer.asStateFlow()

    private val _isAnswered = MutableStateFlow(false)
    val isAnswered: StateFlow<Boolean> = _isAnswered.asStateFlow()

    // Load questions based on mode and bankId
    private val questionsFlow = when (mode) {
        "random", "exam" -> repository.getRandomQuestionsByBank(bankId, 20)
        else -> repository.getQuestionsByBank(bankId)
    }

    val questions: StateFlow<List<Question>> = questionsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentQuestion: StateFlow<Question?> = combine(
        questions, _currentIndex
    ) { list, index ->
        list.getOrElse(index) { null }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val totalQuestions: StateFlow<Int> = combine(
        questions
    ) { it.size }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val isLastQuestion: StateFlow<Boolean> = combine(
        _currentIndex, totalQuestions
    ) { index, total -> index >= total - 1 }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun selectAnswer(answer: String) {
        if (_isAnswered.value) return
        _selectedAnswer.value = answer
        _isAnswered.value = true
    }

    fun nextQuestion() {
        if (!isLastQuestion.value) {
            _currentIndex.value++
            _selectedAnswer.value = null
            _isAnswered.value = false
        }
    }

    fun previousQuestion() {
        if (_currentIndex.value > 0) {
            _currentIndex.value--
            _selectedAnswer.value = null
            _isAnswered.value = false
        }
    }

    fun resetQuiz() {
        _currentIndex.value = 0
        _selectedAnswer.value = null
        _isAnswered.value = false
    }

    fun isCorrect(): Boolean {
        val question = currentQuestion.value ?: return false
        val selected = _selectedAnswer.value ?: return false
        return when (question.type) {
            QuestionType.TRUE_FALSE -> {
                selected.equals(question.answer, ignoreCase = true)
            }
            QuestionType.SHORT_ANSWER -> {
                selected.equals(question.answer, ignoreCase = true)
            }
            else -> selected == question.answer
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val repository: QuestionRepository,
        private val mode: String = "sequential",
        private val bankId: Long = 0,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return QuizViewModel(repository, mode, bankId) as T
        }
    }
}
