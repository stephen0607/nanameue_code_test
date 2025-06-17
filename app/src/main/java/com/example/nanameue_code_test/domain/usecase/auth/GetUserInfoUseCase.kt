package com.example.nanameue_code_test.domain.usecase.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class GetUserInfoUseCase(
) {
    operator fun invoke(): Result<FirebaseUser?> {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return try {
            Result.success(currentUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 