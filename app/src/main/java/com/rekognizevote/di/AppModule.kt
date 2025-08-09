package com.rekognizevote.di

import com.rekognizevote.data.local.SecureStorage
import com.rekognizevote.data.remote.ApiService
import com.rekognizevote.data.repository.AuthRepositoryImpl
import com.rekognizevote.data.repository.PollRepositoryImpl
import com.rekognizevote.data.repository.VoteRepositoryImpl
import com.rekognizevote.domain.repository.AuthRepository
import com.rekognizevote.domain.repository.PollRepository
import com.rekognizevote.domain.repository.VoteRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object AppModule {
    
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
    
    val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }
    
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.rekognizevote.com/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
    
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
    
    lateinit var secureStorage: SecureStorage
    
    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(apiService, secureStorage)
    }
    
    val pollRepository: PollRepository by lazy {
        PollRepositoryImpl(apiService)
    }
    
    val voteRepository: VoteRepository by lazy {
        VoteRepositoryImpl(apiService)
    }
}