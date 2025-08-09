package com.rekognizevote.data.local

import android.content.Context
import com.rekognizevote.domain.model.Poll
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PollCache @Inject constructor(
    @ApplicationContext private val context: Context,
    private val json: Json
) {
    private val cacheDir = File(context.cacheDir, "polls")
    
    init {
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
    }
    
    suspend fun savePolls(polls: List<Poll>) = withContext(Dispatchers.IO) {
        try {
            val file = File(cacheDir, "polls.json")
            val jsonString = json.encodeToString(polls)
            file.writeText(jsonString)
        } catch (e: Exception) {
            // Log error
        }
    }
    
    suspend fun getPolls(): List<Poll>? = withContext(Dispatchers.IO) {
        try {
            val file = File(cacheDir, "polls.json")
            if (file.exists()) {
                val jsonString = file.readText()
                json.decodeFromString<List<Poll>>(jsonString)
            } else null
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun savePoll(poll: Poll) = withContext(Dispatchers.IO) {
        try {
            val file = File(cacheDir, "poll_${poll.id}.json")
            val jsonString = json.encodeToString(poll)
            file.writeText(jsonString)
        } catch (e: Exception) {
            // Log error
        }
    }
    
    suspend fun getPoll(pollId: String): Poll? = withContext(Dispatchers.IO) {
        try {
            val file = File(cacheDir, "poll_$pollId.json")
            if (file.exists()) {
                val jsonString = file.readText()
                json.decodeFromString<Poll>(jsonString)
            } else null
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun clearCache() = withContext(Dispatchers.IO) {
        cacheDir.listFiles()?.forEach { it.delete() }
    }
}