package com.rekognizevote.data.dto

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
data class TokenRefreshRequest(
    val refreshToken: String
)

@Serializable
data class VoteRequest(
    val candidateId: String,
    val faceImageUrl: String
)

@Serializable
data class PollAccessRequest(
    val code: String
)

@Serializable
data class PresignedUrlRequest(
    val type: String = "face_evidence"
)

@Serializable
data class PresignedUrlResponse(
    val uploadUrl: String,
    val imageUrl: String
)