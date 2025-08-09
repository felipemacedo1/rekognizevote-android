package com.rekognizevote.ui.screens.polls

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rekognizevote.core.Result
import com.rekognizevote.domain.model.Poll
import com.rekognizevote.domain.repository.PollRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PollDetailsViewModel @Inject constructor(
    private val pollRepository: PollRepository
) : ViewModel() {
    
    private val _pollState = MutableStateFlow<Result<Poll>>(Result.Loading)
    val pollState: StateFlow<Result<Poll>> = _pollState.asStateFlow()
    
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
    
    fun validateAccessCode(pollId: String, accessCode: String) {
        viewModelScope.launch {
            try {
                pollRepository.validateAccessCode(pollId, accessCode)
            } catch (e: Exception) {
                // Handle error - could show a snackbar or update state
            }
        }
    }
}