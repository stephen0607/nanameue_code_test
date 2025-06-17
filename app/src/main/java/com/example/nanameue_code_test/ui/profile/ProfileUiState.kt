package com.example.nanameue_code_test.ui.profile

import com.google.firebase.auth.FirebaseUser

data class ProfileUiState(
    val userInfo: FirebaseUser? = null,
    val isLoading: Boolean = false
)