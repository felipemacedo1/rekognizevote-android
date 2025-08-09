package com.rekognizevote.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: User,
    val uploadUrl: String? = null // Para upload da selfie no registro
)

@Serializable
data class TokenRefreshRequest(
    val refreshToken: String
)

@Serializable
data class TokenRefreshResponse(
    val accessToken: String,
    val refreshToken: String
)