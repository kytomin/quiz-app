package com.example.quiz.data.model

import kotlinx.serialization.Serializable

@Serializable
data class QuestionModel(
    val text: String,
    val answers: List<String>,
    val correctAnswerIndex: Int,
    val correctAnswerComment: String? = null,
    var userAnswerIndex: Int? = null
)