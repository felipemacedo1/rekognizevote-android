package com.rekognizevote.data.repository

import android.content.Context
import com.rekognizevote.core.Result
import com.rekognizevote.data.remote.ApiService
import com.rekognizevote.domain.model.Poll
import com.rekognizevote.domain.model.Candidate
import com.rekognizevote.domain.repository.PollRepository

class PollRepositoryImpl(
    private val apiService: ApiService,
    private val context: Context
) : PollRepository {
    
    override suspend fun getPolls(status: String): Result<List<Poll>> {
        return try {
            // MODO MOCK - Dados simulados
            kotlinx.coroutines.delay(800)
            
            val mockPolls = listOf(
                Poll(
                    id = "poll_1",
                    title = "Eleição Presidencial 2024",
                    description = "Escolha o próximo presidente",
                    status = "active",
                    candidates = listOf(
                        Candidate("1", "Candidato A", "Descrição A", null, 45, 45f),
                        Candidate("2", "Candidato B", "Descrição B", null, 55, 55f)
                    ),
                    isPrivate = false,
                    startDate = "2024-01-01T00:00:00Z",
                    endDate = "2024-12-31T23:59:59Z",
                    createdAt = "2024-01-01T00:00:00Z"
                ),
                Poll(
                    id = "poll_2",
                    title = "Enquete Municipal",
                    description = "Votação para prefeito",
                    status = "active",
                    candidates = listOf(
                        Candidate("3", "Candidato C", "Descrição C", null, 30, 30f),
                        Candidate("4", "Candidato D", "Descrição D", null, 70, 70f)
                    ),
                    isPrivate = true,
                    accessCode = "1234",
                    startDate = "2024-01-01T00:00:00Z",
                    endDate = "2024-12-31T23:59:59Z",
                    createdAt = "2024-01-01T00:00:00Z"
                )
            )
            
            Result.Success(mockPolls)
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