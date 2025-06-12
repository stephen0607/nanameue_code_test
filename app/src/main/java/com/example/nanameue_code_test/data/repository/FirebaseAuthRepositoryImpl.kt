package com.example.nanameue_code_test.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : FirebaseAuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun signIn(email: String, password: String): Result<FirebaseUser> =
        runCatching {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user ?: throw IllegalStateException("User is null after successful sign in")
        }

    override suspend fun signUp(email: String, password: String): Result<FirebaseUser> =
        runCatching {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user ?: throw IllegalStateException("User is null after successful sign up")
        }

    override suspend fun signOut(): Result<Unit> = runCatching {
        firebaseAuth.signOut()
    }

    override suspend fun updateDisplayName(name: String): Result<Unit> {
        val user = FirebaseAuth.getInstance().currentUser
            ?: return Result.failure(Exception("No user logged in"))

        return try {
            user.updateProfile(userProfileChangeRequest {
                displayName = name
            }).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override fun getAuthState(): Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }
} 