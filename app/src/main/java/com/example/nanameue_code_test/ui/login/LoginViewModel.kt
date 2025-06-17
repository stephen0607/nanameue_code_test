package com.example.nanameue_code_test.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanameue_code_test.NavigationEvent
import com.example.nanameue_code_test.domain.usecase.auth.SignInUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class LoginEvent : NavigationEvent() {
    data object NavigateToTimeline : LoginEvent()
    data object NavigateToSignUp : LoginEvent()
    data class Error(val message: String) : LoginEvent()
}

class LoginViewModel(
    private val signInUseCase: SignInUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<LoginEvent>(replay = 1)
    val event: SharedFlow<LoginEvent> = _event

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
        _event.tryEmit(LoginEvent.NavigateToSignUp)
    }

    fun login() {
        val currentState = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            signInUseCase(currentState.email, currentState.password).onSuccess {
                _event.tryEmit(LoginEvent.NavigateToTimeline)
            }.onFailure {
                _event.tryEmit(LoginEvent.Error(it.message ?: "Authentication failed"))
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun onDialogDismissAction() {
        _uiState.update { it.copy(isLoading = false) }
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