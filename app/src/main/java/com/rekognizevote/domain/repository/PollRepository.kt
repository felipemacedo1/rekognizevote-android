package com.rekognizevote.domain.repository

import com.rekognizevote.core.Result
import com.rekognizevote.domain.model.Poll

interface PollRepository {
    suspend fun getPolls(status: String): Result<List<Poll>>
    suspend fun getPoll(pollId: String): Result<Poll>
    suspend fun getPollResults(pollId: String): Result<Poll>
    suspend fun validateAccessCode(pollId: String, accessCode: String): Result<Boolean>
}