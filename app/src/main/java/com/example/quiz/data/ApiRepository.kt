package com.example.quiz.data

import com.example.quiz.api.ApiService
import com.example.quiz.data.model.QuizModel
import com.example.quiz.data.model.QuizPreviewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApiRepository {

    private val apiService by lazy {
        ApiService.create()
    }

    suspend fun getQuizzes(): List<QuizPreviewModel> = withContext(Dispatchers.IO) {
        return@withContext apiService.getQuizzes()
    }

    suspend fun loadQuiz(id: String): QuizModel? = withContext(Dispatchers.IO) {
        return@withContext apiService.getQuiz(id)
    }
}