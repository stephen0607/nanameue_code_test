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
        return authRepository.signUp(email, password)
    }
}