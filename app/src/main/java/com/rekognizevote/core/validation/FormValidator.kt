package com.rekognizevote.core.validation

import android.util.Patterns

object FormValidator {
    
    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult.Error("Email é obrigatório")
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> ValidationResult.Error("Email inválido")
            else -> ValidationResult.Success
        }
    }
    
    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult.Error("Senha é obrigatória")
            password.length < 6 -> ValidationResult.Error("Senha deve ter pelo menos 6 caracteres")
            else -> ValidationResult.Success
        }
    }
    
    fun validateName(name: String): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult.Error("Nome é obrigatório")
            name.length < 2 -> ValidationResult.Error("Nome deve ter pelo menos 2 caracteres")
            else -> ValidationResult.Success
        }
    }
    
    fun validateConfirmPassword(password: String, confirmPassword: String): ValidationResult {
        return when {
            confirmPassword.isBlank() -> ValidationResult.Error("Confirmação de senha é obrigatória")
            password != confirmPassword -> ValidationResult.Error("Senhas não coincidem")
            else -> ValidationResult.Success
        }
    }
}

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}