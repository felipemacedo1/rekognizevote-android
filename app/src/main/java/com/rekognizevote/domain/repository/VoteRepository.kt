package com.rekognizevote.domain.repository

import com.rekognizevote.core.Result
import com.rekognizevote.domain.model.VoteResponse

interface VoteRepository {
    suspend fun submitVote(pollId: String, candidateId: String, imageUri: String): Result<VoteResponse>
    suspend fun uploadImage(imageUri: String): Result<String>
}