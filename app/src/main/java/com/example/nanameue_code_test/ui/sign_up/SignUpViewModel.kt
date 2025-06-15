package com.example.nanameue_code_test.ui.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanameue_code_test.NavigationEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class SignUpEvent : NavigationEvent() {
    data object NavigateBack : SignUpEvent()
    data object NavigateToTimeline : SignUpEvent()
}

sealed class SignUpUiEvent {
    data object Success : SignUpUiEvent()
    data class Error(val message: String) : SignUpUiEvent()
}

class SignUpViewModel : ViewModel() {
    private val _uiEvent = MutableSharedFlow<SignUpUiEvent>(replay = 0)
    val uiEvent: SharedFlow<SignUpUiEvent> = _uiEvent

    private val _uiState = MutableStateFlow<SignUpUiState>(SignUpUiState.Input())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<SignUpEvent>(replay = 1)
    val navigationEvent: SharedFlow<SignUpEvent> = _navigationEvent

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
                isConfirmPasswordValid = password == currentState.confirmPassword,
                isButtonEnabled = currentState.isEmailValid && isPasswordValid &&
                        (password == currentState.confirmPassword)
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

    fun signUpStart() {
        val currentState = _uiState.value
        if (currentState is SignUpUiState.Input) {
            _uiState.value = currentState.copy(isLoading = true)
        }
    }

    fun signUpSuccess() {
        viewModelScope.launch {
            _uiEvent.emit(SignUpUiEvent.Success)
        }
    }

    fun signUpFailed(message: String) {
        viewModelScope.launch {
            _uiEvent.emit(SignUpUiEvent.Error(message))
        }
    }

    fun onDialogDismissAction(){
        val currentState = _uiState.value
        if(currentState is SignUpUiState.Input){
            _uiState.value =  currentState.copy(isLoading = false)
        }
    }

    fun resetUiState() {
        _uiState.value = SignUpUiState.Input()
    }

    fun navigateToTimeline() {
        _navigationEvent.tryEmit(SignUpEvent.NavigateToTimeline)
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
