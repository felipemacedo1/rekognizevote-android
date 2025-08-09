package com.rekognizevote.data.repository

import com.rekognizevote.core.Constants
import com.rekognizevote.core.Result
import com.rekognizevote.data.remote.ApiService
import com.rekognizevote.domain.model.*
import com.rekognizevote.domain.repository.AuthRepository
import com.rekognizevote.utils.SecurePreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val securePreferences: SecurePreferences
) : AuthRepository {
    
    override suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = apiService.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    // Salvar tokens
                    securePreferences.saveString(Constants.ACCESS_TOKEN_KEY, authResponse.accessToken)
                    securePreferences.saveString(Constants.REFRESH_TOKEN_KEY, authResponse.refreshToken)
                    securePreferences.saveString(Constants.USER_ID_KEY, authResponse.user.id)
                    
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
                    securePreferences.saveString(Constants.ACCESS_TOKEN_KEY, authResponse.accessToken)
                    securePreferences.saveString(Constants.REFRESH_TOKEN_KEY, authResponse.refreshToken)
                    securePreferences.saveString(Constants.USER_ID_KEY, authResponse.user.id)
                    
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
                    securePreferences.saveString(Constants.ACCESS_TOKEN_KEY, tokenResponse.accessToken)
                    securePreferences.saveString(Constants.REFRESH_TOKEN_KEY, tokenResponse.refreshToken)
                    
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
        securePreferences.clear()
    }
    
    override suspend fun isLoggedIn(): Boolean {
        val token = securePreferences.getString(Constants.ACCESS_TOKEN_KEY)
        return !token.isNullOrEmpty()
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