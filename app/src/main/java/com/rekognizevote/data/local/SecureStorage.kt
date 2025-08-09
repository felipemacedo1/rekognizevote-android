package com.rekognizevote.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecureStorage(val context: Context) {
    
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    
    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "rekognize_vote_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    
    fun saveToken(token: String) {
        sharedPreferences.edit().putString("access_token", token).apply()
    }
    
    fun getToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }
    
    fun saveRefreshToken(token: String) {
        sharedPreferences.edit().putString("refresh_token", token).apply()
    }
    
    fun getRefreshToken(): String? {
        return sharedPreferences.getString("refresh_token", null)
    }
    
    fun saveUserId(userId: String) {
        sharedPreferences.edit().putString("user_id", userId).apply()
    }
    
    fun getUserId(): String? {
        return sharedPreferences.getString("user_id", null)
    }
    
    fun saveUserEmail(email: String) {
        sharedPreferences.edit().putString("user_email", email).apply()
    }
    
    fun getUserEmail(): String? {
        return sharedPreferences.getString("user_email", null)
    }
    
    fun isLoggedIn(): Boolean {
        return getToken() != null
    }
    
    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }
}