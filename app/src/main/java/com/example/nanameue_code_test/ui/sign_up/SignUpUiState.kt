package com.example.nanameue_code_test.ui.sign_up

sealed class SignUpUiState {
    data class Input(
        val displayName: String = "",
        val email: String = "",
        val password: String = "",
        val confirmPassword: String = "",
        val isEmailValid: Boolean = true,
        val isPasswordValid: Boolean = true,
        val isConfirmPasswordValid: Boolean = true,
        val isButtonEnabled: Boolean = false,
        val isLoading: Boolean = false
    ) : SignUpUiState()
    
    data object Success : SignUpUiState()
    data class Error(val message: String) : SignUpUiState()
}
