package com.example.nanameue_code_test.domain.usecase.auth

import com.example.nanameue_code_test.data.repository.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseUser


class SignUpUseCase(
    private val authRepository: FirebaseAuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
    ): Result<FirebaseUser> {
        if (email.isBlank()) {
            return Result.failure(IllegalArgumentException("Email cannot be empty"))
        }
        if (!email.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)\$"))) {
            return Result.failure(IllegalArgumentException("Invalid email format"))
        }
        if (password.isBlank()) {
            return Result.failure(IllegalArgumentException("Password cannot be empty"))
        }
        return authRepository.signUp(email, password)
    }
}