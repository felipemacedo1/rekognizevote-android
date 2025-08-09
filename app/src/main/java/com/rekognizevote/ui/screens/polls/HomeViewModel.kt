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
class HomeViewModel @Inject constructor(
    private val pollRepository: PollRepository
) : ViewModel() {
    
    private val _pollsState = MutableStateFlow<Result<List<Poll>>>(Result.Loading)
    val pollsState: StateFlow<Result<List<Poll>>> = _pollsState.asStateFlow()
    
    fun loadPolls(status: String) {
        viewModelScope.launch {
            _pollsState.value = Result.Loading
            try {
                val result = pollRepository.getPolls(status)
                _pollsState.value = result
            } catch (e: Exception) {
                _pollsState.value = Result.Error(e)
            }
        }
    }
}