package com.rekognizevote.data.repository

import com.rekognizevote.core.Result
import com.rekognizevote.data.dto.*
import com.rekognizevote.data.local.SecureStorage
import com.rekognizevote.data.remote.ApiService
import com.rekognizevote.domain.model.*
import com.rekognizevote.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val secureStorage: SecureStorage
) : AuthRepository {
    
    override suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = apiService.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    // Salvar tokens
                    secureStorage.saveToken(authResponse.accessToken)
                    secureStorage.saveRefreshToken(authResponse.refreshToken)
                    secureStorage.saveUserId(authResponse.user.id)
                    secureStorage.saveUserEmail(authResponse.user.email)
                    
                    Result.Success(authResponse)
                } ?: Result.Error(Exception("Resposta vazia"))
            } else {
                Result.Error(Exception("Erro de login: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun register(name: String, email: String, password: String): Result<AuthResponse> {
        return try {
            val response = apiService.register(RegisterRequest(name, email, password))
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    // Salvar tokens
                    secureStorage.saveToken(authResponse.accessToken)
                    secureStorage.saveRefreshToken(authResponse.refreshToken)
                    secureStorage.saveUserId(authResponse.user.id)
                    secureStorage.saveUserEmail(authResponse.user.email)
                    
                    Result.Success(authResponse)
                } ?: Result.Error(Exception("Resposta vazia"))
            } else {
                Result.Error(Exception("Erro de registro: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun refreshToken(refreshToken: String): Result<AuthResponse> {
        return try {
            val response = apiService.refreshToken(TokenRefreshRequest(refreshToken))
            if (response.isSuccessful) {
                response.body()?.let { tokenResponse ->
                    // Atualizar tokens
                    secureStorage.saveToken(tokenResponse.accessToken)
                    secureStorage.saveRefreshToken(tokenResponse.refreshToken)
                    
                    // Buscar dados do usuário
                    val userResponse = apiService.getCurrentUser()
                    if (userResponse.isSuccessful) {
                        userResponse.body()?.let { user ->
                            Result.Success(AuthResponse(
                                tokenResponse.accessToken,
                                tokenResponse.refreshToken,
                                user
                            ))
                        } ?: Result.Error(Exception("Erro ao buscar usuário"))
                    } else {
                        Result.Error(Exception("Erro ao buscar usuário"))
                    }
                } ?: Result.Error(Exception("Resposta vazia"))
            } else {
                Result.Error(Exception("Erro ao renovar token: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun logout() {
        secureStorage.clearAll()
    }
    
    override suspend fun isLoggedIn(): Boolean {
        return secureStorage.isLoggedIn()
    }
    
    override suspend fun getCurrentUser(): Result<User> {
        return try {
            val response = apiService.getCurrentUser()
            if (response.isSuccessful) {
                response.body()?.let { user ->
                    Result.Success(user)
                } ?: Result.Error(Exception("Resposta vazia"))
            } else {
                Result.Error(Exception("Erro ao buscar usuário: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}