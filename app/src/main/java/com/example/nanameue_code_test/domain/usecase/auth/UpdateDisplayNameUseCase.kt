package com.example.nanameue_code_test.domain.usecase.auth

import com.example.nanameue_code_test.data.repository.FirebaseAuthRepository

class UpdateDisplayNameUseCase(
    private val authRepository: FirebaseAuthRepository
) {
    suspend operator fun invoke(displayName: String): Result<Unit> {
        return try {
            authRepository.updateDisplayName(displayName)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 