package com.rekognizevote.data.repository

import com.rekognizevote.core.Result
import com.rekognizevote.data.dto.*
import com.rekognizevote.data.remote.ApiService
import com.rekognizevote.domain.model.Vote
import com.rekognizevote.domain.repository.VoteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoteRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : VoteRepository {
    
    override suspend fun submitVote(pollId: String, candidateId: String, faceImageUrl: String): Result<Vote> {
        return try {
            val response = apiService.vote(pollId, VoteRequest(candidateId, faceImageUrl))
            if (response.isSuccessful) {
                response.body()?.let { vote ->
                    Result.Success(vote)
                } ?: Result.Error(Exception("Resposta vazia"))
            } else {
                Result.Error(Exception("Erro ao votar: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun getPresignedUrl(): Result<PresignedUrlResponse> {
        return try {
            val response = apiService.getPresignedUrl("face_evidence")
            if (response.isSuccessful) {
                response.body()?.let { urlResponse ->
                    Result.Success(urlResponse)
                } ?: Result.Error(Exception("Resposta vazia"))
            } else {
                Result.Error(Exception("Erro ao obter URL: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}