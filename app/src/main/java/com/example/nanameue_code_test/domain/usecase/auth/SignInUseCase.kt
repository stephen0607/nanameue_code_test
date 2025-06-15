package com.example.nanameue_code_test.domain.usecase.auth

import com.example.nanameue_code_test.data.repository.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseUser

class SignInUseCase(
    private val authRepository: FirebaseAuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<FirebaseUser> {
        if (email.isBlank()) {
            return Result.failure(IllegalArgumentException("Email cannot be empty"))
        }
        if (password.isBlank()) {
            return Result.failure(IllegalArgumentException("Password cannot be empty"))
        }
        return authRepository.signIn(email, password)
    }
}