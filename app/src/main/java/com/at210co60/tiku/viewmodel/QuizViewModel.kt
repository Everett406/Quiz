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

    private val _selectedAnswers = MutableStateFlow<Set<String>>(emptySet())
    val selectedAnswers: StateFlow<Set<String>> = _selectedAnswers.asStateFlow()

    private val _isAnswered = MutableStateFlow(false)
    val isAnswered: StateFlow<Boolean> = _isAnswered.asStateFlow()

    private val _quizCompleted = MutableStateFlow(false)
    val quizCompleted: StateFlow<Boolean> = _quizCompleted.asStateFlow()

    private val _correctCount = MutableStateFlow(0)
    val correctCount: StateFlow<Int> = _correctCount.asStateFlow()

    private val _totalAnswered = MutableStateFlow(0)
    val totalAnswered: StateFlow<Int> = _totalAnswered.asStateFlow()

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
        val question = currentQuestion.value ?: return
        if (question.type == QuestionType.MULTI_CHOICE) {
            _selectedAnswers.value = _selectedAnswers.value.toMutableSet().apply {
                if (contains(answer)) remove(answer) else add(answer)
            }
        } else {
            _selectedAnswers.value = setOf(answer)
            _isAnswered.value = true
            _totalAnswered.value++
            if (isCorrect()) {
                _correctCount.value++
            }
        }
    }

    fun confirmMultiChoiceAnswer() {
        if (_isAnswered.value) return
        _isAnswered.value = true
        _totalAnswered.value++
        if (isCorrect()) {
            _correctCount.value++
        }
    }

    fun nextQuestion() {
        if (!isLastQuestion.value) {
            _currentIndex.value++
            _selectedAnswers.value = emptySet()
            _isAnswered.value = false
        } else {
            _quizCompleted.value = true
        }
    }

    fun previousQuestion() {
        if (_currentIndex.value > 0) {
            _currentIndex.value--
            _selectedAnswers.value = emptySet()
            _isAnswered.value = false
        }
    }

    fun finishQuiz() {
        _quizCompleted.value = true
    }

    fun resetQuiz() {
        _currentIndex.value = 0
        _selectedAnswers.value = emptySet()
        _isAnswered.value = false
        _quizCompleted.value = false
        _correctCount.value = 0
        _totalAnswered.value = 0
    }

    fun isCorrect(): Boolean {
        val question = currentQuestion.value ?: return false
        val selected = _selectedAnswers.value
        if (selected.isEmpty()) return false
        return when (question.type) {
            QuestionType.MULTI_CHOICE -> {
                selected.size == question.answers.size && selected.containsAll(question.answers)
            }
            QuestionType.TRUE_FALSE -> {
                selected.firstOrNull()?.equals(question.answers.firstOrNull(), ignoreCase = true) ?: false
            }
            QuestionType.SHORT_ANSWER -> {
                selected.firstOrNull()?.equals(question.answers.firstOrNull(), ignoreCase = true) ?: false
            }
            else -> selected.firstOrNull() == question.answers.firstOrNull()
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
