package com.rekognizevote.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Vote(
    val id: String,
    val pollId: String,
    val candidateId: String,
    val userId: String,
    val evidenceKey: String,
    val verificationScore: Float,
    val timestamp: String
)

@Serializable
data class VoteResponse(
    val success: Boolean,
    val message: String,
    val voteId: String? = null
)