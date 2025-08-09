package com.rekognizevote.data.repository

import android.content.Context
import com.rekognizevote.core.Result
import com.rekognizevote.data.remote.ApiService
import com.rekognizevote.domain.model.Poll
import com.rekognizevote.domain.repository.PollRepository

class PollRepositoryImpl(
    private val apiService: ApiService,
    private val context: Context
) : PollRepository {
    
    override suspend fun getPolls(status: String): Result<List<Poll>> {
        return try {
            val response = apiService.getPolls(status)
            if (response.isSuccessful) {
                response.body()?.let { polls ->
                    Result.Success(polls)
                } ?: Result.Error(Exception("Resposta vazia"))
            } else {
                Result.Error(Exception("Erro ao buscar enquetes: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun getPoll(pollId: String): Result<Poll> {
        return try {
            val response = apiService.getPoll(pollId)
            if (response.isSuccessful) {
                response.body()?.let { poll ->
                    Result.Success(poll)
                } ?: Result.Error(Exception("Resposta vazia"))
            } else {
                Result.Error(Exception("Erro ao buscar enquete: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun getPollResults(pollId: String): Result<Poll> {
        return try {
            val response = apiService.getPollResults(pollId)
            if (response.isSuccessful) {
                response.body()?.let { poll ->
                    Result.Success(poll)
                } ?: Result.Error(Exception("Resposta vazia"))
            } else {
                Result.Error(Exception("Erro ao buscar resultados: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun validateAccessCode(pollId: String, accessCode: String): Result<Boolean> {
        return try {
            val response = apiService.validateAccessCode(pollId, accessCode)
            if (response.isSuccessful) {
                Result.Success(true)
            } else {
                Result.Error(Exception("Código de acesso inválido"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}