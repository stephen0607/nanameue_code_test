package com.example.nanameue_code_test.ui.create_post

import android.net.Uri

data class CreatePostUiState(
    val postContent: String = "",
    val imageUri: Uri? = null,
    val isPostButtonEnable: Boolean = false,
    val status: CreatePostStatus = CreatePostStatus.INPUT,
    val errorMessage: String? = null
)

enum class CreatePostStatus {
    INPUT,
    LOADING,
    SUCCESS,
    ERROR
}