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
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _registerState = MutableStateFlow<Result<AuthResponse>>(Result.Loading)
    val registerState: StateFlow<Result<AuthResponse>> = _registerState.asStateFlow()
    
    init {
        _registerState.value = Result.Success(AuthResponse("", "", 
            com.rekognizevote.domain.model.User("", "", "", "", "")))
    }
    
    fun register(name: String, email: String, password: String, confirmPassword: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _registerState.value = Result.Error(Exception("Preencha todos os campos"))
            return
        }
        
        if (password != confirmPassword) {
            _registerState.value = Result.Error(Exception("As senhas não coincidem"))
            return
        }
        
        if (password.length < 6) {
            _registerState.value = Result.Error(Exception("A senha deve ter pelo menos 6 caracteres"))
            return
        }
        
        viewModelScope.launch {
            _registerState.value = Result.Loading
            try {
                val result = authRepository.register(name, email, password)
                _registerState.value = result
            } catch (e: Exception) {
                _registerState.value = Result.Error(e)
            }
        }
    }
}