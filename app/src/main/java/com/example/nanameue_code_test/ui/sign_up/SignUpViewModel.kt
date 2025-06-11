package com.example.nanameue_code_test.ui.sign_up

import androidx.lifecycle.ViewModel
import com.example.nanameue_code_test.NavigationEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SignUpUiState(
    val email: String = "testtest@gmail.com",
    val password: String = "123123123",
    val confirmPassword: String = "123123123",
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isConfirmPasswordValid: Boolean = false,
    val isButtonEnabled: Boolean = false
)

sealed class SignUpEvent : NavigationEvent() {
    data object NavigateBack : SignUpEvent()
    data object NavigateToLogin : SignUpEvent()
}

class SignUpViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<SignUpEvent>(replay = 1)
    val navigationEvent: SharedFlow<SignUpEvent> = _navigationEvent

    fun updateEmail(email: String) {
        val isEmailValid = email.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)\$"))
        _uiState.update { currentState ->
            currentState.copy(
                email = email,
                isEmailValid = isEmailValid,
                isButtonEnabled = isEmailValid && currentState.isPasswordValid && currentState.isConfirmPasswordValid
            )
        }
    }

    fun updatePassword(password: String) {
        val isPasswordValid = password.length >= 8
        _uiState.update { currentState ->
            currentState.copy(
                password = password,
                isPasswordValid = isPasswordValid,
                isConfirmPasswordValid = password == currentState.confirmPassword,
                isButtonEnabled = currentState.isEmailValid && isPasswordValid &&
                        (password == currentState.confirmPassword)
            )
        }
    }

    fun updateConfirmPassword(confirmPassword: String) {
        val isConfirmPasswordValid = confirmPassword == _uiState.value.password
        _uiState.update { currentState ->
            currentState.copy(
                confirmPassword = confirmPassword,
                isConfirmPasswordValid = isConfirmPasswordValid,
                isButtonEnabled = currentState.isEmailValid && currentState.isPasswordValid && isConfirmPasswordValid
            )
        }
    }

    fun navigateBack() {
        _navigationEvent.tryEmit(SignUpEvent.NavigateBack)
    }

    fun navigationToLogin(){
        _navigationEvent.tryEmit(SignUpEvent.NavigateToLogin)

    }
}