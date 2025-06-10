package com.example.nanameue_code_test.ui.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

sealed class LoginEvent {
    object NavigateToTimeline : LoginEvent()
    object NavigateToSignUp : LoginEvent()
}

class LoginViewModel : ViewModel() {
    private val _navigationEvent = MutableSharedFlow<LoginEvent>(replay = 1)
    val navigationEvent: SharedFlow<LoginEvent> = _navigationEvent

    fun signUp() {
        _navigationEvent.tryEmit(LoginEvent.NavigateToSignUp)
    }

    fun login() {
        _navigationEvent.tryEmit(LoginEvent.NavigateToTimeline)
    }
} 