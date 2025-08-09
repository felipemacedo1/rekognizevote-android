package com.rekognizevote.ui.screens.vote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rekognizevote.core.Result
import com.rekognizevote.domain.model.Poll
import com.rekognizevote.domain.model.VoteResponse
import com.rekognizevote.domain.repository.PollRepository
import com.rekognizevote.domain.repository.VoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VoteViewModel @Inject constructor(
    private val pollRepository: PollRepository,
    private val voteRepository: VoteRepository
) : ViewModel() {
    
    private val _pollState = MutableStateFlow<Result<Poll>>(Result.Loading)
    val pollState: StateFlow<Result<Poll>> = _pollState.asStateFlow()
    
    private val _voteState = MutableStateFlow<Result<VoteResponse>>(Result.Loading)
    val voteState: StateFlow<Result<VoteResponse>> = _voteState.asStateFlow()
    
    init {
        _voteState.value = Result.Success(VoteResponse(true, ""))
    }
    
    fun loadPoll(pollId: String) {
        viewModelScope.launch {
            _pollState.value = Result.Loading
            try {
                val result = pollRepository.getPoll(pollId)
                _pollState.value = result
            } catch (e: Exception) {
                _pollState.value = Result.Error(e)
            }
        }
    }
    
    fun submitVote(pollId: String, candidateId: String, imageUri: String) {
        viewModelScope.launch {
            _voteState.value = Result.Loading
            try {
                val result = voteRepository.submitVote(pollId, candidateId, imageUri)
                _voteState.value = result
            } catch (e: Exception) {
                _voteState.value = Result.Error(e)
            }
        }
    }
}