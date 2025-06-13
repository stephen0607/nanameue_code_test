package com.example.nanameue_code_test.domain.usecase.auth

import com.example.nanameue_code_test.data.repository.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

class GetAuthStateUseCase(
    private val authRepository: FirebaseAuthRepository
) {
    operator fun invoke(): Flow<FirebaseUser?> {
        return authRepository.getAuthState()
    }
} 