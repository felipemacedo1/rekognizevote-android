package com.rekognizevote.data.interceptor

import com.rekognizevote.data.local.SecureStorage
import com.rekognizevote.data.remote.ApiService
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenRefreshInterceptor @Inject constructor(
    private val secureStorage: SecureStorage,
    private val apiService: ApiService
) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        
        if (response.code == 401 && secureStorage.getRefreshToken() != null) {
            response.close()
            
            return runBlocking {
                try {
                    val refreshToken = secureStorage.getRefreshToken()!!
                    val tokenResponse = apiService.refreshToken(
                        com.rekognizevote.data.dto.TokenRefreshRequest(refreshToken)
                    )
                    
                    if (tokenResponse.isSuccessful) {
                        tokenResponse.body()?.let { newTokens ->
                            secureStorage.saveToken(newTokens.accessToken)
                            secureStorage.saveRefreshToken(newTokens.refreshToken)
                            
                            val newRequest = request.newBuilder()
                                .header("Authorization", "Bearer ${newTokens.accessToken}")
                                .build()
                            
                            chain.proceed(newRequest)
                        } ?: response
                    } else {
                        secureStorage.clearAll()
                        response
                    }
                } catch (e: Exception) {
                    secureStorage.clearAll()
                    response
                }
            }
        }
        
        return response
    }
}