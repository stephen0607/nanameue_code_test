package com.example.nanameue_code_test.domain.usecase.auth

import com.example.nanameue_code_test.data.repository.FirebaseAuthRepository

class SignOutUseCase(
    private val authRepository: FirebaseAuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return try {
            authRepository.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
