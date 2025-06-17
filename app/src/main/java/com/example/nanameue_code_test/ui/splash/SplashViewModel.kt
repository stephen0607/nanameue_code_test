package com.example.nanameue_code_test.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanameue_code_test.NavigationEvent
import com.example.nanameue_code_test.domain.usecase.auth.GetAuthStateUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

sealed class SplashEvent : NavigationEvent() {
    data object NavigateToTimeline : SplashEvent()
    data object NavigateToLogin : SplashEvent()
}

class SplashViewModel(
    private val getAuthStateUseCase: GetAuthStateUseCase
) : ViewModel() {
    private val _navigationEvent = MutableSharedFlow<SplashEvent>(replay = 1)
    val navigationEvent: SharedFlow<SplashEvent> = _navigationEvent

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        viewModelScope.launch {
            getAuthStateUseCase().collect { user ->
                _navigationEvent.tryEmit(
                    if (user != null) {
                        SplashEvent.NavigateToTimeline
                    } else {
                        SplashEvent.NavigateToLogin
                    }
                )
            }
        }
    }
}