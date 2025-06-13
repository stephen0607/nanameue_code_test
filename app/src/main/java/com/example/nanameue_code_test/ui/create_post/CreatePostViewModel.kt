package com.example.nanameue_code_test.ui.create_post

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanameue_code_test.NavigationEvent
import com.example.nanameue_code_test.domain.usecase.create_post.CreatePostUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class CreatePostEvent : NavigationEvent() {
    data object NavigateToTimeline : CreatePostEvent()
}

class CreatePostViewModel(
    private val createPostUseCase: CreatePostUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreatePostUiState())
    val uiState: StateFlow<CreatePostUiState> = _uiState

    private val _navigationEvent = MutableSharedFlow<CreatePostEvent>(replay = 1)
    val navigationEvent: SharedFlow<CreatePostEvent> = _navigationEvent

    fun onPostContentChanged(content: String) {
        _uiState.value = _uiState.value.copy(
            postContent = content,
            isPostButtonEnable = content.isNotBlank() || _uiState.value.imageUri != null
        )
    }

    fun onImageSelected(uri: Uri?) {
        _uiState.value = _uiState.value.copy(
            imageUri = uri,
            isPostButtonEnable = uri != null || _uiState.value.postContent.isNotBlank()
        )
    }

    fun removeImage() {
        onImageSelected(null)
    }

    fun createPost() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(status = CreatePostStatus.LOADING)
            val state = _uiState.value
            try {
                createPostUseCase.execute(state.postContent, state.imageUri)
                _uiState.value = CreatePostUiState(status = CreatePostStatus.SUCCESS)
                _navigationEvent.emit(CreatePostEvent.NavigateToTimeline)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    status = CreatePostStatus.ERROR,
                    errorMessage = e.message ?: "Something went wrong"
                )
            }
        }
    }

    fun navigateToTimeline() {
        _navigationEvent.tryEmit(CreatePostEvent.NavigateToTimeline)
    }

    fun resetUiState() {
        _uiState.value = CreatePostUiState()
    }
}
