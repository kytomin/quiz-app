package com.example.quiz.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiz.data.ApiRepository
import com.example.quiz.data.model.QuestionModel
import com.example.quiz.data.model.QuizModel
import com.example.quiz.states.UIState
import kotlinx.coroutines.launch


class QuizViewModel : ViewModel() {

    private val repository: ApiRepository = ApiRepository()

    private val _uiState = MutableLiveData<UIState>()
    var uiState: LiveData<UIState> = _uiState

    private lateinit var quiz: QuizModel

    private val _name = MutableLiveData<String>()
    var name: LiveData<String> = _name

    private var currentQuestionIndex: Int = 0

    private val _quizCompleted = MutableLiveData(false)
    var quizCompleted: LiveData<Boolean> = _quizCompleted

    private val _progress = MutableLiveData(0)
    var progress: LiveData<Int> = _progress

    private val _currentQuestion = MutableLiveData<QuestionModel?>()
    var currentQuestion: LiveData<QuestionModel?> = _currentQuestion

    private val _countCorrectAnswers = MutableLiveData(0)
    var countCorrectAnswers: LiveData<Int> = _countCorrectAnswers

    private val _numberOfQuestions = MutableLiveData<Int>()
    var numberOfQuestions: LiveData<Int> = _numberOfQuestions

    fun loadQuiz(id: String) {


        viewModelScope.launch {
            try {
                _uiState.value = UIState.Loading

                currentQuestionIndex = 0
                _progress.value = 0
                _quizCompleted.value = false
                _countCorrectAnswers.value = 0
                _name.value = ""
                _currentQuestion.value = null

                val temp = repository.loadQuiz(id)
                if (temp == null) {
                    _uiState.value = UIState.Failure
                    return@launch
                }
                quiz = temp
                _currentQuestion.value = quiz.questions[currentQuestionIndex]
                _numberOfQuestions.value = quiz.questions.size
                _name.value = quiz.name
                _uiState.value = UIState.Success
            } catch (e: Exception) {
                _uiState.value = UIState.Failure
            }
        }
    }

    fun getQuestionByIndex(index: Int): QuestionModel {
        return quiz.questions[index]
    }

    fun onClickAnswer(answerIndex: Int) {

        if (_uiState.value != UIState.Success)
            return
        /*
         If there was no answer, or it was wrong, but it became right
        */
        if (quiz.questions[currentQuestionIndex].correctAnswerIndex == answerIndex
            && quiz.questions[currentQuestionIndex].userAnswerIndex != answerIndex
        ) {
            _countCorrectAnswers.value = _countCorrectAnswers.value?.plus(1)
        }

        /*
        If the answer was correct, but became incorrect
         */
        if (quiz.questions[currentQuestionIndex].correctAnswerIndex != answerIndex
            && quiz.questions[currentQuestionIndex].userAnswerIndex == quiz.questions[currentQuestionIndex].correctAnswerIndex
        ) {
            _countCorrectAnswers.value = _countCorrectAnswers.value?.minus(1)
        }

        quiz.questions[currentQuestionIndex].userAnswerIndex = answerIndex

        /*
        If the question is not the last
         */
        if (currentQuestionIndex < quiz.questions.size - 1) {
            currentQuestionIndex++
            _currentQuestion.value = quiz.questions[currentQuestionIndex]
            _progress.value = 100 * currentQuestionIndex / quiz.questions.size
        }
        /*
        If the question is the last one
         */
        else {
            _progress.value = 100
            _quizCompleted.value = true
        }
    }

    fun onPrevious() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex -= 1
            _currentQuestion.value = quiz.questions[currentQuestionIndex]
            _progress.value = 100 * currentQuestionIndex / quiz.questions.size
            _quizCompleted.value = false
        }
    }

    fun onAgain() {
        loadQuiz(quiz.id)
    }
}

