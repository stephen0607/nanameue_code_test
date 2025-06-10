package com.example.nanameue_code_test.ui.sign_up

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

sealed class SignUpEvent {
    object NavigateBack : SignUpEvent()
}

class SignUpViewModel : ViewModel() {
    private val _navigationEvent = MutableSharedFlow<SignUpEvent>(replay = 1)
    val navigationEvent: SharedFlow<SignUpEvent> = _navigationEvent
    fun navBack() {
        _navigationEvent.tryEmit(SignUpEvent.NavigateBack)
    }

    fun signUp() {
        // TODO: Implement sign up logic
    }
}