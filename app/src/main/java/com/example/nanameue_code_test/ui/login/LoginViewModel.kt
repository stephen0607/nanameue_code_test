package com.example.nanameue_code_test.ui.login

import androidx.lifecycle.ViewModel
import com.example.nanameue_code_test.NavigationEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


sealed class LoginEvent : NavigationEvent() {
    data object NavigateToTimeline : LoginEvent()
    data object NavigateToSignUp : LoginEvent()
}

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<LoginEvent>(replay = 1)
    val navigationEvent: SharedFlow<LoginEvent> = _navigationEvent

    fun updateEmail(email: String) {
        val isEmailValid = email.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)\$"))
        _uiState.update { currentState ->
            currentState.copy(
                email = email,
                isEmailValid = isEmailValid,
                isButtonEnabled = isEmailValid && currentState.isPasswordValid
            )
        }
    }

    fun updatePassword(password: String) {
        val isPasswordValid = password.length >= 8
        _uiState.update { currentState ->
            currentState.copy(
                password = password,
                isPasswordValid = isPasswordValid,
                isButtonEnabled = currentState.isEmailValid && isPasswordValid
            )
        }
    }

    fun signUp() {
        _navigationEvent.tryEmit(LoginEvent.NavigateToSignUp)
    }

    fun login() {
        _navigationEvent.tryEmit(LoginEvent.NavigateToTimeline)
    }

    fun resetUiState() {
        _uiState.value = LoginUiState()
    }

    // todo remove after testing
    fun autoFillForTesting() {
        _uiState.update { currentState ->
            currentState.copy(
                email = "22222abc@abcde.com",
                password = "12312312",
                isPasswordValid = true,
                isEmailValid = true,
                isButtonEnabled = true
            )
        }
    }
} 