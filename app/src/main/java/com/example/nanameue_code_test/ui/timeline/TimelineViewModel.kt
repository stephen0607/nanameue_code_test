package com.example.nanameue_code_test.ui.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanameue_code_test.NavigationEvent
import com.example.nanameue_code_test.domain.usecase.GetLatestPostsUseCase
import com.example.nanameue_code_test.domain.usecase.Post
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// UI State
sealed class TimelineUiState {
    data object Loading : TimelineUiState()
    data class Success(val posts: List<Post>) : TimelineUiState()
    data class Error(val message: String) : TimelineUiState()
}

sealed class TimelineEvent : NavigationEvent() {
    object NavigateToProfile : TimelineEvent()
    object NavigateToCreatePost : TimelineEvent()
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
                _uiState.value = TimelineUiState.Success(postList)
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
