package com.example.nanameue_code_test.ui.create_post

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanameue_code_test.NavigationEvent
import com.example.nanameue_code_test.domain.usecase.CreatePostUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class CreatePostEvent : NavigationEvent() {
    data object NavigateToTimeline : CreatePostEvent()
}

data class CreatePostUiState(
    val postContent: String = "",
    val imageUri: Uri? = null
)


class CreatePostViewModel(
    private val createPostUseCase: CreatePostUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreatePostUiState())
    val uiState: StateFlow<CreatePostUiState> = _uiState

    private val _navigationEvent = MutableSharedFlow<CreatePostEvent>(replay = 1)
    val navigationEvent: SharedFlow<CreatePostEvent> = _navigationEvent

    fun onPostContentChanged(content: String) {
        _uiState.value = _uiState.value.copy(postContent = content)
    }

    fun onImageSelected(uri: Uri?) {
        _uiState.value = _uiState.value.copy(imageUri = uri)
    }

    fun removeImage() {
        onImageSelected(null)
    }

    fun createPost() {
        viewModelScope.launch {
            val state = _uiState.value

            createPostUseCase.execute(state.postContent, state.imageUri)
            _navigationEvent.emit(CreatePostEvent.NavigateToTimeline)
        }
    }

    fun navigateToTimeline() {
        _navigationEvent.tryEmit(CreatePostEvent.NavigateToTimeline)
    }
}
