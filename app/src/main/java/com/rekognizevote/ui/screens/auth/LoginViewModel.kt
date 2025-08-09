package com.rekognizevote.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rekognizevote.core.Result
import com.rekognizevote.domain.model.AuthResponse
import com.rekognizevote.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _loginState = MutableStateFlow<Result<AuthResponse>>(Result.Loading)
    val loginState: StateFlow<Result<AuthResponse>> = _loginState.asStateFlow()
    
    init {
        _loginState.value = Result.Success(AuthResponse("", "", 
            com.rekognizevote.domain.model.User("", "", "", "", "")))
    }
    
    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = Result.Error(Exception("Preencha todos os campos"))
            return
        }
        
        viewModelScope.launch {
            _loginState.value = Result.Loading
            try {
                val result = authRepository.login(email, password)
                _loginState.value = result
            } catch (e: Exception) {
                _loginState.value = Result.Error(e)
            }
        }
    }
}