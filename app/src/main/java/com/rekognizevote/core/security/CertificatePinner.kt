package com.rekognizevote.core.security

import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object CertificatePinner {
    
    private const val API_HOSTNAME = "api.rekognizevote.com"
    private const val DEV_HOSTNAME = "dev-api.rekognizevote.com"
    
    private const val PROD_PIN = "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA="
    private const val DEV_PIN = "sha256/BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB="
    
    fun createSecureOkHttpClient(isDebug: Boolean = false): OkHttpClient {
        val certificatePinner = if (isDebug) {
            CertificatePinner.Builder().build()
        } else {
            CertificatePinner.Builder()
                .add(API_HOSTNAME, PROD_PIN)
                .add(DEV_HOSTNAME, DEV_PIN)
                .build()
        }
        
        return OkHttpClient.Builder()
            .certificatePinner(certificatePinner)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
}