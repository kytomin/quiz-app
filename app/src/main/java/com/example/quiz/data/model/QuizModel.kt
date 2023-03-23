package com.example.quiz.data.model

import kotlinx.serialization.Serializable

@Serializable
data class QuizModel(
    var id: String,
    var name: String,
    var questions: List<QuestionModel>,
)