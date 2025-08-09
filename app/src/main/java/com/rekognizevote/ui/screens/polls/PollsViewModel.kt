package com.rekognizevote.ui.screens.polls

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rekognizevote.core.Result
import com.rekognizevote.domain.model.Poll
import com.rekognizevote.domain.repository.PollRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PollsViewModel(
    private val pollRepository: PollRepository
) : ViewModel() {
    
    private val _pollsState = MutableStateFlow<Result<List<Poll>>>(Result.Loading)
    val pollsState: StateFlow<Result<List<Poll>>> = _pollsState
    
    init {
        loadPolls()
    }
    
    fun loadPolls(status: String = "active") {
        viewModelScope.launch {
            _pollsState.value = Result.Loading
            _pollsState.value = pollRepository.getPolls(status)
        }
    }
    
    fun refreshPolls() {
        loadPolls()
    }
}