package com.rekognizevote.data.repository

import com.rekognizevote.core.Result
import com.rekognizevote.data.dto.*
import com.rekognizevote.data.local.SecureStorage
import com.rekognizevote.data.remote.ApiService
import com.rekognizevote.domain.model.*
import com.rekognizevote.domain.repository.AuthRepository
class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val secureStorage: SecureStorage
) : AuthRepository {
    
    override suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            // MODO MOCK - Simular login bem-sucedido
            kotlinx.coroutines.delay(1000) // Simular delay de rede
            
            if (email.isNotEmpty() && password.isNotEmpty()) {
                val mockUser = User(
                    id = "mock_user_123",
                    name = "Usuário Teste",
                    email = email,
                    createdAt = "2024-01-01T00:00:00Z"
                )
                val mockAuthResponse = AuthResponse(
                    accessToken = "mock_access_token_123",
                    refreshToken = "mock_refresh_token_123",
                    user = mockUser
                )
                
                // Salvar tokens mock
                secureStorage.saveToken(mockAuthResponse.accessToken)
                secureStorage.saveRefreshToken(mockAuthResponse.refreshToken)
                secureStorage.saveUserId(mockAuthResponse.user.id)
                secureStorage.saveUserEmail(mockAuthResponse.user.email)
                
                Result.Success(mockAuthResponse)
            } else {
                Result.Error(Exception("Email e senha são obrigatórios"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun register(name: String, email: String, password: String): Result<AuthResponse> {
        return try {
            // MODO MOCK - Simular registro bem-sucedido
            kotlinx.coroutines.delay(1000) // Simular delay de rede
            
            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val mockUser = User(
                    id = "mock_user_${System.currentTimeMillis()}",
                    name = name,
                    email = email,
                    createdAt = "2024-01-01T00:00:00Z"
                )
                val mockAuthResponse = AuthResponse(
                    accessToken = "mock_access_token_${System.currentTimeMillis()}",
                    refreshToken = "mock_refresh_token_${System.currentTimeMillis()}",
                    user = mockUser
                )
                
                // Salvar tokens mock
                secureStorage.saveToken(mockAuthResponse.accessToken)
                secureStorage.saveRefreshToken(mockAuthResponse.refreshToken)
                secureStorage.saveUserId(mockAuthResponse.user.id)
                secureStorage.saveUserEmail(mockAuthResponse.user.email)
                
                Result.Success(mockAuthResponse)
            } else {
                Result.Error(Exception("Todos os campos são obrigatórios"))
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