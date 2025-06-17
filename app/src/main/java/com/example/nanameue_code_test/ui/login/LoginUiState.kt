package com.example.nanameue_code_test.ui.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isButtonEnabled: Boolean = false,
    val isLoading: Boolean = false
)