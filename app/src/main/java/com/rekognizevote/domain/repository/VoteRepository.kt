package com.rekognizevote.domain.repository

import com.rekognizevote.core.Result
import com.rekognizevote.data.dto.PresignedUrlResponse
import com.rekognizevote.domain.model.Vote

interface VoteRepository {
    suspend fun submitVote(pollId: String, candidateId: String, faceImageUrl: String): Result<Vote>
    suspend fun getPresignedUrl(): Result<PresignedUrlResponse>
}