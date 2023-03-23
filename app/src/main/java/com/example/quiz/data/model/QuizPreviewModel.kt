package com.example.quiz.data.model

import kotlinx.serialization.Serializable

@Serializable
data class QuizPreviewModel(
    var id: String,
    var name: String,
    var imageUrl: String
)