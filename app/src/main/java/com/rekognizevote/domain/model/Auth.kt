package com.rekognizevote.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: User
)

@Serializable
data class TokenRefreshResponse(
    val accessToken: String,
    val refreshToken: String
)