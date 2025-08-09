package com.rekognizevote.di

import com.rekognizevote.BuildConfig
import com.rekognizevote.core.security.CertificatePinner
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
        val baseClient = CertificatePinner.createSecureOkHttpClient(BuildConfig.DEBUG)
        
        baseClient.newBuilder()
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }
            }
            .build()
    }
    
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
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
        PollRepositoryImpl(apiService, secureStorage.context)
    }
    
    val voteRepository: VoteRepository by lazy {
        VoteRepositoryImpl(apiService)
    }
}