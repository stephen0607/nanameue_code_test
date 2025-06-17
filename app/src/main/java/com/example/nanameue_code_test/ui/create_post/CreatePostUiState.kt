package com.example.nanameue_code_test.ui.create_post

import android.net.Uri

sealed class CreatePostUiState {
    data class Input(
        val postContent: String = "",
        val imageUri: Uri? = null,
        val isPostButtonEnable: Boolean = false
    ) : CreatePostUiState()

    data object Loading : CreatePostUiState()
    data object Success : CreatePostUiState()
    data class Error(val message: String) : CreatePostUiState()
} 