package com.rekognizevote

import android.app.Application
import com.rekognizevote.data.local.SecureStorage
import com.rekognizevote.di.AppModule
import timber.log.Timber

class RekognizeVoteApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        
        AppModule.secureStorage = SecureStorage(this)
    }
}