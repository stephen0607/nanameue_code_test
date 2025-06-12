package com.example.nanameue_code_test.data.repository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthRepository {
    val currentUser: FirebaseUser?

    suspend fun signIn(email: String, password: String): Result<FirebaseUser>
    suspend fun signUp(email: String, password: String): Result<FirebaseUser>
    suspend fun signOut(): Result<Unit>
    suspend fun updateDisplayName(name: String): Result<Unit>
    fun getAuthState(): Flow<FirebaseUser?>
} 