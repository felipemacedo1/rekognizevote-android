package com.rekognizevote.domain.repository

import com.rekognizevote.core.Result
import com.rekognizevote.domain.model.AuthResponse

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<AuthResponse>
    suspend fun register(name: String, email: String, password: String): Result<AuthResponse>
    suspend fun refreshToken(refreshToken: String): Result<AuthResponse>
    suspend fun logout()
    suspend fun isLoggedIn(): Boolean
    suspend fun getCurrentUser(): Result<com.rekognizevote.domain.model.User>
}