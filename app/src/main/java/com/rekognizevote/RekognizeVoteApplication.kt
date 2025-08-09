package com.rekognizevote

import android.app.Application
import com.rekognizevote.core.monitoring.CrashReporter
import com.rekognizevote.data.local.SecureStorage
import com.rekognizevote.di.AppModule

class RekognizeVoteApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize crash reporting and logging
        CrashReporter.initialize(this, BuildConfig.DEBUG)
        
        // Initialize DI
        AppModule.secureStorage = SecureStorage(this)
        
        CrashReporter.logEvent("app_started")
    }
}