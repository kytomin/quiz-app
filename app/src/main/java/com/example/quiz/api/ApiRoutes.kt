package com.example.quiz.api

object ApiRoutes {
    private const val BASE_URL = "https://quiz.meowplex.com/api"
    const val QUIZZES = "$BASE_URL/get_quizzes"
    fun QUIZ(quizId: String) = "$BASE_URL/get_quiz/$quizId"
}