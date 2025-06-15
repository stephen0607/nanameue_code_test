package com.example.nanameue_code_test.ui.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanameue_code_test.NavigationEvent
import com.example.nanameue_code_test.domain.usecase.timeline.GetLatestPostsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class TimelineEvent : NavigationEvent() {
    data object NavigateToProfile : TimelineEvent()
    data object NavigateToCreatePost : TimelineEvent()
}

class TimelineViewModel(
    private val getLatestPostsUseCase: GetLatestPostsUseCase
) : ViewModel() {
    private val _navigationEvent = MutableSharedFlow<TimelineEvent>(replay = 1)
    val navigationEvent: SharedFlow<TimelineEvent> = _navigationEvent

    private val _uiState = MutableStateFlow<TimelineUiState>(TimelineUiState.Loading)
    val uiState: StateFlow<TimelineUiState> = _uiState

    fun fetchTimeline() {
        viewModelScope.launch {
            _uiState.value = TimelineUiState.Loading
            val result = getLatestPostsUseCase.execute()
            result.onSuccess { postList ->
                if (postList.isEmpty()) {
                    _uiState.value = TimelineUiState.NoPost
                } else {
                    _uiState.value = TimelineUiState.Success(postList)
                }
            }.onFailure { e ->
                _uiState.value = TimelineUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun navigateToProfile() {
        _navigationEvent.tryEmit(TimelineEvent.NavigateToProfile)
    }

    fun navigateToCreatePost() {
        _navigationEvent.tryEmit(TimelineEvent.NavigateToCreatePost)
    }
}
