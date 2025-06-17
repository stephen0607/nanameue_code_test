package com.example.nanameue_code_test.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanameue_code_test.NavigationEvent
import com.example.nanameue_code_test.domain.usecase.auth.GetUserInfoUseCase
import com.example.nanameue_code_test.domain.usecase.auth.SignOutUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class ProfileEvent : NavigationEvent() {
    data object NavigateToTimeline : ProfileEvent()
    data object NavigateToLogin : ProfileEvent()
    data class Error(val message: String) : ProfileEvent()
}


class ProfileViewModel(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ProfileEvent>(replay = 0)
    val event: SharedFlow<ProfileEvent> = _event

    init {
        loadUserInfo()
    }

    private fun loadUserInfo() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getUserInfoUseCase()
                .onSuccess { userInfo ->
                    _uiState.update { it.copy(userInfo = userInfo, isLoading = false) }
                    if (userInfo == null) {
                        _event.emit(ProfileEvent.NavigateToLogin)
                    }
                }
                .onFailure {
                    _event.emit(ProfileEvent.Error(it.message ?: "Failed to load user info"))
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            signOutUseCase()
                .onSuccess {
                    _event.emit(ProfileEvent.NavigateToLogin)
                }
                .onFailure {
                    _event.emit(ProfileEvent.Error(it.message ?: "Failed to sign out"))
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }
}