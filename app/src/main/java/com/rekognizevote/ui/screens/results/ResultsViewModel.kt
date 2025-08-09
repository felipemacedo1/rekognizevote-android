package com.rekognizevote.ui.screens.results

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
class ResultsViewModel @Inject constructor(
    private val pollRepository: PollRepository
) : ViewModel() {
    
    private val _pollState = MutableStateFlow<Result<Poll>>(Result.Loading)
    val pollState: StateFlow<Result<Poll>> = _pollState.asStateFlow()
    
    fun loadPollResults(pollId: String) {
        viewModelScope.launch {
            _pollState.value = Result.Loading
            try {
                val result = pollRepository.getPollResults(pollId)
                _pollState.value = result
            } catch (e: Exception) {
                _pollState.value = Result.Error(e)
            }
        }
    }
}