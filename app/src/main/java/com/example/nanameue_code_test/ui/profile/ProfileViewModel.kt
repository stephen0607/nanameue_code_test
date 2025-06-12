package com.example.nanameue_code_test.ui.profile

import androidx.lifecycle.ViewModel
import com.example.nanameue_code_test.NavigationEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow


sealed class ProfileEvent : NavigationEvent() {
    data object NavigateToTimeline : ProfileEvent()
    data object NavigateToLogin : ProfileEvent()
}

class ProfileViewModel : ViewModel() {
    private val _navigationEvent = MutableSharedFlow<ProfileEvent>(replay = 1)
    val navigationEvent: SharedFlow<ProfileEvent> = _navigationEvent

    fun navigateToTimeline() {
        _navigationEvent.tryEmit(ProfileEvent.NavigateToTimeline)
    }

    fun navigateToLogin() {
        _navigationEvent.tryEmit(ProfileEvent.NavigateToLogin)
    }

    fun onSignOutComplete(){
//       todo 1.clean shared preference  2.navigation
        navigateToLogin()
    }
}