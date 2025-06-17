package com.example.nanameue_code_test.ui.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanameue_code_test.NavigationEvent
import com.example.nanameue_code_test.domain.usecase.auth.SignUpUseCase
import com.example.nanameue_code_test.domain.usecase.auth.UpdateDisplayNameUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class SignUpEvent : NavigationEvent() {
    data object NavigateBack : SignUpEvent()
    data object NavigateToTimeline : SignUpEvent()
    data class Error(val message: String) : SignUpEvent()
}

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase,
    private val updateDisplayNameUseCase: UpdateDisplayNameUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<SignUpUiState>(SignUpUiState.Input())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SignUpEvent>(replay = 0)
    val event: SharedFlow<SignUpEvent> = _event

    fun updateDisplayName(displayName: String) {
        val currentState = _uiState.value
        if (currentState is SignUpUiState.Input) {
            _uiState.value = currentState.copy(displayName = displayName)
        }
    }

    fun updateEmail(email: String) {
        val currentState = _uiState.value
        if (currentState is SignUpUiState.Input) {
            val isEmailValid = email.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)\$"))
            _uiState.value = currentState.copy(
                email = email,
                isEmailValid = isEmailValid,
                isButtonEnabled = isEmailValid && currentState.isPasswordValid && currentState.isConfirmPasswordValid
            )
        }
    }

    fun updatePassword(password: String) {
        val currentState = _uiState.value
        if (currentState is SignUpUiState.Input) {
            val isPasswordValid = password.length >= 8
            _uiState.value = currentState.copy(
                password = password,
                isPasswordValid = isPasswordValid,
                isButtonEnabled = currentState.isEmailValid && isPasswordValid && currentState.isConfirmPasswordValid
            )
        }
    }

    fun updateConfirmPassword(confirmPassword: String) {
        val currentState = _uiState.value
        if (currentState is SignUpUiState.Input) {
            val isConfirmPasswordValid = confirmPassword == currentState.password
            _uiState.value = currentState.copy(
                confirmPassword = confirmPassword,
                isConfirmPasswordValid = isConfirmPasswordValid,
                isButtonEnabled = currentState.isEmailValid && currentState.isPasswordValid && isConfirmPasswordValid
            )
        }
    }

    fun signUp() {
        val currentState = _uiState.value
        if (currentState is SignUpUiState.Input) {
            viewModelScope.launch {
                _uiState.value = currentState.copy(isLoading = true)
                signUpUseCase(
                    email = currentState.email,
                    password = currentState.password,
                ).onSuccess {
                    resetUiState()
                    updateDisplayNameUseCase(currentState.displayName)
                        .onFailure { error ->
                            _event.emit(
                                SignUpEvent.Error(
                                    error.message ?: "Failed to update display name"
                                )
                            )
                            return@onSuccess
                        }
                    _event.emit(SignUpEvent.NavigateToTimeline)
                }.onFailure {
                    _event.emit(SignUpEvent.Error(it.message ?: "Registration failed"))
                }
                _uiState.value = currentState.copy(isLoading = false)
            }
        }
    }

    fun onDialogDismissAction() {
        val currentState = _uiState.value
        if (currentState is SignUpUiState.Input) {
            _uiState.value = currentState.copy(isLoading = false)
        }
    }

    fun resetUiState() {
        _uiState.value = SignUpUiState.Input()
    }

    fun autoFillSignUpForTesting() {
        _uiState.value = SignUpUiState.Input(
            displayName = "abc",
            email = "abc@abcde.com",
            password = "12312312",
            confirmPassword = "12312312",
            isConfirmPasswordValid = true,
            isPasswordValid = true,
            isEmailValid = true,
            isButtonEnabled = true
        )
    }
}
