package com.example.quiz.api

import android.util.Log
import com.example.quiz.data.model.QuizModel
import com.example.quiz.data.model.QuizPreviewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*

class ApiServiceImpl(private val client: HttpClient) : ApiService {

    override suspend fun getQuizzes(): List<QuizPreviewModel> {
        return try {
            client.get { url(ApiRoutes.QUIZZES) }.body()
        } catch (ex: RedirectResponseException) {
            // 3xx - responses
            Log.d("ApiService", ex.response.status.description)
            emptyList()
        } catch (ex: ClientRequestException) {
            // 4xx - responses
            Log.d("ApiService", ex.response.status.description)
            emptyList()
        } catch (ex: ServerResponseException) {
            // 5xx - response
            Log.d("ApiService", ex.response.status.description)
            emptyList()
        }
    }

    override suspend fun getQuiz(id: String): QuizModel? {
        return try {
            client.get { url(ApiRoutes.QUIZ(id)) }.body()
        } catch (ex: RedirectResponseException) {
            // 3xx - responses
            Log.d("ApiService", ex.response.status.description)
            null
        } catch (ex: ClientRequestException) {
            // 4xx - responses
            Log.d("ApiService", ex.response.status.description)
            null
        } catch (ex: ServerResponseException) {
            // 5xx - response
            Log.d("ApiService", ex.response.status.description)
            null
        }
    }
}