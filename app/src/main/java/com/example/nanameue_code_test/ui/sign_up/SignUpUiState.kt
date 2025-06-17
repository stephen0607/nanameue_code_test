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
        // put loading here to make sure the loading is on top of input
        val isLoading: Boolean = false
    ) : SignUpUiState()

    data class Error(val message: String) : SignUpUiState()
}