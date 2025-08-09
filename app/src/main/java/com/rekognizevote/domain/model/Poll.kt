package com.rekognizevote.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Poll(
    val id: String,
    val title: String,
    val description: String,
    val isPrivate: Boolean,
    val accessCode: String? = null,
    val status: String, // active, upcoming, closed
    val startDate: String,
    val endDate: String,
    val candidates: List<Candidate>,
    val totalVotes: Int = 0,
    val hasUserVoted: Boolean = false,
    val createdAt: String
)

@Serializable
data class Candidate(
    val id: String,
    val name: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val voteCount: Int = 0,
    val percentage: Float = 0f
)