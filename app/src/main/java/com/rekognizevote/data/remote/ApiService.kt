package com.rekognizevote.data.remote

import com.rekognizevote.data.dto.*
import com.rekognizevote.domain.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
    
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
    
    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: TokenRefreshRequest): Response<TokenRefreshResponse>
    
    @GET("auth/me")
    suspend fun getCurrentUser(): Response<User>
    
    @GET("polls")
    suspend fun getPolls(@Query("status") status: String): Response<List<Poll>>
    
    @GET("polls/{id}")
    suspend fun getPoll(@Path("id") pollId: String): Response<Poll>
    
    @GET("polls/{id}/results")
    suspend fun getPollResults(@Path("id") pollId: String): Response<Poll>
    
    @POST("polls/{id}/access")
    suspend fun validateAccessCode(
        @Path("id") pollId: String,
        @Query("code") accessCode: String
    ): Response<Unit>
    
    @POST("polls/{id}/vote")
    suspend fun vote(
        @Path("id") pollId: String,
        @Body request: VoteRequest
    ): Response<Vote>
    
    @GET("upload/presigned-url")
    suspend fun getPresignedUrl(@Query("type") type: String): Response<PresignedUrlResponse>
}

