package com.rekognizevote.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val faceKey: String? = null,
    val createdAt: String
)