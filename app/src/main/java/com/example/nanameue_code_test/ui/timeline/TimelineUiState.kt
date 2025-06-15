package com.example.nanameue_code_test.ui.timeline

import com.example.nanameue_code_test.domain.usecase.timeline.Post

sealed class TimelineUiState {
    data object Loading : TimelineUiState()
    data class Success(val posts: List<Post>) : TimelineUiState()
    data class Error(val message: String) : TimelineUiState()
    data object NoPost : TimelineUiState()
}