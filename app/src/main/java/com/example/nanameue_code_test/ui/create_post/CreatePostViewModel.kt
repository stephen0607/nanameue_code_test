package com.example.nanameue_code_test.ui.create_post

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanameue_code_test.NavigationEvent
import com.example.nanameue_code_test.domain.usecase.create_post.ICreatePostUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class CreatePostEvent : NavigationEvent() {
    data object NavigateToTimeline : CreatePostEvent()
}

open class CreatePostViewModel(
    private val createPostUseCase: ICreatePostUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CreatePostUiState>(CreatePostUiState.Input())
    val uiState: StateFlow<CreatePostUiState> = _uiState

    private val _navigationEvent = MutableSharedFlow<CreatePostEvent>(replay = 1)
    val navigationEvent: SharedFlow<CreatePostEvent> = _navigationEvent

    fun onPostContentChanged(content: String) {
        val currentState = _uiState.value
        if (currentState is CreatePostUiState.Input) {
            _uiState.value = currentState.copy(
                postContent = content,
                isPostButtonEnable = content.isNotBlank() || currentState.imageUri != null
            )
        }
    }

    fun onImageSelected(uri: Uri?) {
        val currentState = _uiState.value
        if (currentState is CreatePostUiState.Input) {
            _uiState.value = currentState.copy(
                imageUri = uri,
                isPostButtonEnable = uri != null || currentState.postContent.isNotBlank()
            )
        }
    }

    fun removeImage() {
        onImageSelected(null)
    }

    fun createPost() {
        val currentState = _uiState.value
        if (currentState is CreatePostUiState.Input) {
            viewModelScope.launch {
                _uiState.value = CreatePostUiState.Loading
                try {
                    createPostUseCase.execute(currentState.postContent, currentState.imageUri)
                    _uiState.value = CreatePostUiState.Success
                } catch (e: Exception) {
                    _uiState.value = CreatePostUiState.Error(e.message ?: "Something went wrong")
                }
            }
        }
    }

    fun createPostSuccessAction() {
        _navigationEvent.tryEmit(CreatePostEvent.NavigateToTimeline)
    }

    fun resetUiState() {
        _uiState.value = CreatePostUiState.Input()
    }
}
