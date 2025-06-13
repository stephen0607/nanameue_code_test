package com.example.nanameue_code_test.ui.sign_up

enum class SignUpStatus {
    INPUT, LOADING, SUCCESS, ERROR
}

data class SignUpUiState(
    val displayName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isEmailValid: Boolean = true,
    val isPasswordValid: Boolean = true,
    val isConfirmPasswordValid: Boolean = true,
    val isButtonEnabled: Boolean = false,
    val status: SignUpStatus = SignUpStatus.INPUT,
    val errorMessage: String? = null
)
