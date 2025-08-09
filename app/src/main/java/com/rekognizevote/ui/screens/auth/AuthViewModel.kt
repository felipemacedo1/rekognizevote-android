package com.rekognizevote.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rekognizevote.core.Result
import com.rekognizevote.core.validation.FormValidator
import com.rekognizevote.core.validation.ValidationResult
import com.rekognizevote.domain.model.AuthResponse
import com.rekognizevote.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState
    
    fun login(email: String, password: String) {
        val emailValidation = FormValidator.validateEmail(email)
        val passwordValidation = FormValidator.validatePassword(password)
        
        if (emailValidation is ValidationResult.Error) {
            _uiState.value = _uiState.value.copy(emailError = emailValidation.message)
            return
        }
        if (passwordValidation is ValidationResult.Error) {
            _uiState.value = _uiState.value.copy(passwordError = passwordValidation.message)
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, emailError = null, passwordError = null)
            
            when (val result = authRepository.login(email, password)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        authResponse = result.data
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exception.message
                    )
                }
            }
        }
    }
    
    fun register(name: String, email: String, password: String, confirmPassword: String) {
        val nameValidation = FormValidator.validateName(name)
        val emailValidation = FormValidator.validateEmail(email)
        val passwordValidation = FormValidator.validatePassword(password)
        val confirmPasswordValidation = FormValidator.validateConfirmPassword(password, confirmPassword)
        
        if (nameValidation is ValidationResult.Error) {
            _uiState.value = _uiState.value.copy(nameError = nameValidation.message)
            return
        }
        if (emailValidation is ValidationResult.Error) {
            _uiState.value = _uiState.value.copy(emailError = emailValidation.message)
            return
        }
        if (passwordValidation is ValidationResult.Error) {
            _uiState.value = _uiState.value.copy(passwordError = passwordValidation.message)
            return
        }
        if (confirmPasswordValidation is ValidationResult.Error) {
            _uiState.value = _uiState.value.copy(confirmPasswordError = confirmPasswordValidation.message)
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true, 
                error = null, 
                nameError = null, 
                emailError = null, 
                passwordError = null, 
                confirmPasswordError = null
            )
            
            when (val result = authRepository.register(name, email, password)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        authResponse = result.data
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exception.message
                    )
                }
            }
        }
    }
    
    fun checkAuthStatus() {
        viewModelScope.launch {
            val isLoggedIn = authRepository.isLoggedIn()
            _uiState.value = _uiState.value.copy(isAuthenticated = isLoggedIn)
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val authResponse: AuthResponse? = null,
    val error: String? = null,
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null
)