package com.rekognizevote.core.monitoring

import android.content.Context
import timber.log.Timber

object CrashReporter {
    
    fun initialize(context: Context, isDebug: Boolean) {
        if (isDebug) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }
    
    fun logError(throwable: Throwable, message: String? = null) {
        Timber.e(throwable, message ?: "Error occurred")
    }
    
    fun logEvent(event: String, parameters: Map<String, Any> = emptyMap()) {
        Timber.d("Event: $event, Parameters: $parameters")
    }
    
    private class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority >= android.util.Log.WARN) {
                // Send to crash reporting service
            }
        }
    }
}