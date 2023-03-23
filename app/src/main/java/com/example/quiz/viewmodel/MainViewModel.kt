package com.example.quiz.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiz.data.ApiRepository
import com.example.quiz.data.model.QuizPreviewModel
import com.example.quiz.states.UIState
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repository: ApiRepository = ApiRepository()

    private val _uiState = MutableLiveData<UIState>()
    val uiState: LiveData<UIState> = _uiState

    private val _quizzes = MutableLiveData<List<QuizPreviewModel>>()
    var quizzes: LiveData<List<QuizPreviewModel>> = _quizzes

    fun loadQuizzes() {
        viewModelScope.launch {
            try {
                _uiState.value = UIState.Loading

                _quizzes.value = repository.getQuizzes()

                _uiState.value = UIState.Success
            } catch (e: Exception) {
                _uiState.value = UIState.Failure
            }
        }
    }

}